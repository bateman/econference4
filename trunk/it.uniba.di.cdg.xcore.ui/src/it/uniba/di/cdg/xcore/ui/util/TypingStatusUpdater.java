/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.xcore.ui.util;

import it.uniba.di.cdg.xcore.ui.views.ITalkView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread which is designed to receive typing events, to track their timing out and 
 * update the status text of a {@see it.uniba.di.cdg.xcore.ui.views.ITalkView } accordingly. 
 */
public class TypingStatusUpdater extends Thread {
    /**
     * One second, in milliseconds.
     */
    public static final int ONE_SECOND = 1000;
    
    /**
     * How much time will wait between each timing update.
     */
    public static final int UPDATE_PAUSE = 2 * ONE_SECOND;  
    
    /**
     * The default time out associated to each entry, in <b>seconds</b>.
     */
    private static final int DEFAULT_TIMEOUT = 2;
    
    /**
     * An entry for tracking the time out of each entry.
     */
    private static class TimingEntry {
        private int time;
        
        private String userId;
        
        public TimingEntry( String userId, int time ) {
            this.userId = userId;
            this.time = time;
        }

        /**
         * @return Returns the userId.
         */
        public String getUserId() {
            return userId;
        }

        /**
         * Decrease the timeout for this entry.
         */
        public void decreaseTime() {
            --time;
        }

        /**
         * Check if the entry is timed out.
         * 
         * @return <code>true</code> if the entry timed out, <code>false</code> otherwise 
         */
        public boolean isTimedOut() {
            return time <= 0;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals( Object other ) {
            if (other instanceof TimingEntry) {
                final TimingEntry that = (TimingEntry) other;
                return this.userId.equals( that.userId );
            }
            return false;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return userId.hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return userId;
        }
    }

    /**
     * Flag for signaling thread termination.
     */
    private boolean quit;

    /**
     * Lock when updating the timings.
     */
    private final Lock lock;

    /**
     * The timing entries.
     */
    private final List<TimingEntry> timings; 

    /**
     * The talk view to update for events.
     */
    private final ITalkView talkView;
    
    /**
     * Makes up a new thread to handle typing events.
     * 
     * @param talkView the view to update
     */
    public TypingStatusUpdater( ITalkView talkView ) {
        super( "typing event updater" );
        this.lock = new ReentrantLock();
        this.timings = new ArrayList<TimingEntry>();
        this.quit = false;
        this.talkView = talkView;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        while (!quit) {
            try {
//                System.out.println( "UPD: Sleeping 1 sec before updating ..." );
                sleep( UPDATE_PAUSE ); // Wait a few time
//                System.out.println( "UPD: Awoken!" );
            } catch (InterruptedException e) {
                e.printStackTrace(); /* I hate checked exceptions */
            } 
            update(); // Update counters
        }
    }

    /**
     * Queue a user typing with a specific timing
     * 
     * @param user the user name (needs not to be unique)
     * @param timing the timing, in seconds
     */
    public void queueUser( String user, int timing ) {
//        System.out.println( String.format( "UPD: queuing %s from thread %s", user, Thread.currentThread() ) );
        lock.lock();
        try {
            TimingEntry entry = find( user );
            if (entry == null) {
                entry = new TimingEntry( user, timing );
//                System.out.println( String.format( "UPD: New entry! Adding from thread %s", Thread.currentThread() ) );
                timings.add( entry );
            }
            else {
//                System.out.println( String.format( "UPD: Resetting timing for %s", entry ) );
                entry.time = timing;
            }
                
        } finally {
            lock.unlock();
        }
    }

    /**
     * Queue a user typing event using <code>DEFAULT_TIMEOUT</code>.
     *  
     * @param user the user name
     */
    public void queueUser( String user ) {
        queueUser( user, DEFAULT_TIMEOUT );
    }
    
    /**
     * Returns the composed text.
     *  
     * @return the composed text (maybe "" if no entry is present)
     */
    public String composeStatusText() {
        lock.lock();
        try {
            if (1 == timings.size()) {
                String user = timings.get( 0 ).getUserId();
                return user + " is typing ...";
            } else if ( timings.size() > 1) {
                Iterator<TimingEntry> it = timings.iterator();
                StringBuffer sb = new StringBuffer( it.next().getUserId() );
                for (; it.hasNext(); ) {
                    sb.append( ", " );
                    sb.append( it.next().getUserId() );
                }
                sb.append( " are typing ..." );
                
                return sb.toString();
            }
        } finally {
            lock.unlock();
        }
        return "";
    }

    /**
     * Quit this thread.
     */
    public void quit() {
        this.quit = true;
    }

    /**
     * Search the timing entry for the specified user id.
     * 
     * @param user
     * @return the timing entry or <code>null</code> if not present
     */
    private TimingEntry find( String user ) {
        for (TimingEntry entry : timings) {
            if (entry.getUserId().equals( user ) )
                return entry;
        }
        return null;
    }
    
    /**
     * Update all timings.
     */
    private void update() {
//        System.out.println( String.format( "UPD: Updating timings from thread %s", Thread.currentThread() ) );
        lock.lock();
        try {
//            System.out.println( String.format( "UPD: [BEFORE] entries = %s", timings ) );
            boolean needUpdate = false;
            for (Iterator<TimingEntry> it = timings.iterator(); it.hasNext();) {
                final TimingEntry entry = it.next();
                entry.decreaseTime();
                
                if (entry.isTimedOut()) {
//                    System.out.println( String.format( "UPD: %s died!", entry ) );
                    it.remove();
                    needUpdate = true;
                }
            }
            if (needUpdate) {
//                System.out.println( "UPD: Removed entry: needs update ..." );
                talkView.updateStatus( composeStatusText() );
            }
        } finally {
//            System.out.println( String.format( "UPD: [AFTER] entries = %s", timings ) );
            lock.unlock();
        }
    }
}
