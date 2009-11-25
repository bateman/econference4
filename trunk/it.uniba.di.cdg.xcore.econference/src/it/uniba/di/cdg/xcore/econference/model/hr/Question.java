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
package it.uniba.di.cdg.xcore.econference.model.hr;

import org.eclipse.core.runtime.PlatformObject;


/**
 * Implementation of {@see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion}.
 * <p>
 * Note that since id must be unique we rely of the {@see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel}
 * implementation for providing one that is valid.
 * <p>
 * Note also that we extend <code>PlatformObject</code> to the hierarchy to make the class usable
 * by the Eclipse framework during UI notifications.
 */
public class Question extends PlatformObject implements IQuestion {
    /**
     * The question uid.
     */
    private final int id;
    
    /**
     * The text contained in this question.
     */
    private String text;
    
    /**
     * The id of the participant that has raised the question.
     */
    private String who;

    /**
     * The status for the question, initially <code>PENDING</code>.
     */
    private QuestionStatus status;
    
    /**
     * The parent model this question belongs to.
     */
    private IHandRaisingModel model;
    
    /**
     * Create a new question without specifying question and who has made (clients will have
     * to use the appropriate setters though, to ensure proper validity).
     * 
     * @param id the unique id for this question
     */
    public Question( int id ) {
        this( null, id, "", "", QuestionStatus.PENDING );
    }

    /**
     * Create a new question using constructor injection.
     * 
     * @param model the parent model 
     * @param id the unique id for this question 
     * @param text
     * @param who
     * @param status
     */
    public Question( IHandRaisingModel model, int id, String text, String who, QuestionStatus status ) {
        this.model = model;
        this.id = id;
        this.text = text;
        this.who = who;
        this.status = status;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#getId()
     */
    public int getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#getText()
     */
    public String getText() {
        return text;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#setText(java.lang.String)
     */
    public void setText( String text ) {
        this.text = text;
        notifyChange();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#getUserId()
     */
    public String getWho() {
        return who;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#setUserId(java.lang.String)
     */
    public void setWho( String who ) {
        this.who = who;
        notifyChange();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (!(other instanceof IQuestion))
            return false;
        IQuestion that = (IQuestion) other;
        return this.getId() == that.getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#setModel(it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel)
     */
    public void setModel( IHandRaisingModel model ) {
        this.model = model;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#getModel()
     */
    public IHandRaisingModel getModel() {
        return model;
    }
    
    private void notifyChange() {
        if (model != null)
            model.notifyQuestionChanged( this );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#setStatus(it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus)
     */
    public void setStatus( QuestionStatus qs ) {
        this.status = qs;
        notifyChange();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion#getStatus()
     */
    public QuestionStatus getStatus() {
        return status;
    }
}
