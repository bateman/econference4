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
package it.uniba.di.cdg.xcore.network.model.tv;

import it.uniba.di.cdg.xcore.aspects.ThreadSafetyAspect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.network.model.tv.ITalkModel}.
 */
public class TalkModel implements ITalkModel {
    
    private Map<String, List<Entry>> entriesByThread;

    private String currentThreadId;
    
    private Comparator<Entry> entryComparator = new Comparator<Entry>() {
        public int compare( Entry e1, Entry e2 ) {
            return e1.getTimestamp().compareTo( e2.getTimestamp() );
        }
    };

    private Set<ITalkModelListener> listeners;
    
    public TalkModel() {
        this.entriesByThread = new HashMap<String, List<Entry>>();
        this.currentThreadId = FREE_TALK_THREAD_ID;
        this.listeners = new HashSet<ITalkModelListener>();
    }
    
    public void addEntry( Entry entry ) {
        addEntry( getCurrentThread(), entry );
    }

    public void addEntry( String threadId, Entry entry ) {
        List<Entry> entries = getOrCreateEntryList( threadId );
        entries.add( entry );
        
        for (ITalkModelListener l : listeners)
            l.entryAdded( threadId, entry );
    }

    private List<Entry> getOrCreateEntryList( String threadId ) {
        List<Entry> entries = entriesByThread.get( threadId );
        if (entries == null) { // unknown thread id
            entries = new ArrayList<Entry>();
            entriesByThread.put( threadId, entries );
        }
        return entries;
    }

    public List<Entry> getCurrentThreadEntries() {
        return getEntries( getCurrentThread() );
    }

    public List<Entry> getEntries( String threadId ) {
        List<Entry> entries = getOrCreateEntryList( threadId );
        
        return Collections.unmodifiableList(  entries );
    }

    public List<Entry> getAllEntries() {
        final List<Entry> allEntries = new ArrayList<Entry>();

        for (String thread: entriesByThread.keySet()) {
            allEntries.addAll( getEntries( thread ) );
        }
        
        Collections.sort( allEntries, entryComparator );
        
        return allEntries;
    }

    public void setCurrentThread( String threadId ) {
        for (ITalkModelListener l : listeners)
            l.currentThreadChanged( getCurrentThread(), threadId );

        this.currentThreadId = threadId;
    }

    public String getCurrentThread() {
        return currentThreadId;
    }

    public void addListener( ITalkModelListener l ) {
        listeners.add( l );
    }

    public void removeListener( ITalkModelListener l ) {
        listeners.remove( l );
    }
    
    /**
     * Provides internal thread synchronization.
     */
    @Aspect
    public static class OwnThreadSafety extends ThreadSafetyAspect {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
         */
        @Override
        @Pointcut( "execution( public * TalkModel.get*(..) )" )
        protected void readOperations() {}

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
         */
        @Override
        @Pointcut( "execution( public void TalkModel.set*(..) )" +
                "|| execution( public void TalkModel.add*(..) )" +
                "|| execution( public void TalkModel.remove*(..) )" )
        protected void writeOperations() {}
    }
}
