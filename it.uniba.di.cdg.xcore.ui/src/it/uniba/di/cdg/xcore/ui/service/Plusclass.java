package it.uniba.di.cdg.xcore.ui.service;

/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;


/**
 * @author Malerba Francesco

 */
public class Plusclass {
  private static final Logger log = Logger.getLogger(Plusclass.class.getName());

  private static Plus plus;
  private static Plus unauthenticatedPlus;

  @SuppressWarnings("unused")
  private static void getImage(Plus p , String id) throws IOException{
	  
	  Person pers = p.people().get(id).execute();
	  pers.getImage().getUrl();
	  
  }
  
  /**
   * Still not used, can be used to find a person
   * 
   * @param account the plus object
   * @param nome person name and surname
   * @param mail person email
   */
  @SuppressWarnings("unused")
  private static void cercapersona(Plus account, String nome , String mail ){
	  try {
		Plus.People.Search searchPeople = account.people().search();
		boolean peopleFound = false;
		searchPeople.setQuery(nome);
		searchPeople.setMaxResults(5L);
		
		PeopleFeed peopleFeed = searchPeople.execute();
		List<Person> people = peopleFeed.getItems();

		// Loop through until we arrive at an empty page, or the second page
		int pageNumber = 1;
		while (people != null && pageNumber <= 2) {
		  pageNumber++;
		  
		  for (Person person : people) {
		    System.out.println(person.getDisplayName());
		    if (person.containsValue(mail)){
		    	System.out.print("Trovato :");
		    	show(person);
		  }
		    
		  }

		  // We will know we are on the last page when the next page token is null.
		  // If this is the case, break.
		  if (peopleFeed.getNextPageToken() == null) {
		    break;
		  }

		  // Prepare the next page of results
		  searchPeople.setPageToken(peopleFeed.getNextPageToken());

		  // Execute and process the next page request
		  peopleFeed = searchPeople.execute();
		  people = peopleFeed.getItems();
		}
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	  
  }
  
  
  /**
   * 
   * 
   * @param autorizzazione Auth object obtained and validated through the authentication protocol
   * @return a Plus object that can be used to make queries, get information etc.
   */

 @SuppressWarnings(value = { "deprecation" })
public static Plus getPlus(Auth autorizzazione)  {
	    // Here's an example of an unauthenticated Plus object. In cases where you
	    // do not need to use the /me/ path segment to discover the current user's
	    // ID, you can skip the OAuth flow with this code.
	    unauthenticatedPlus = new Plus(Util.TRANSPORT, Util.JSON_FACTORY);
	    // When we do not specify access tokens, we must specify our API key instead
	    
	    unauthenticatedPlus.setKey(Auth.GOOGLE_API_KEY);

	    // If, however, you need to use OAuth to identify the current user you must
	    // create the Plus object differently. Most programs will need only one
	    // of these since you can use an authenticated Plus object for any call.
	    
	    GoogleAccessProtectedResource requestInitializer =
	        new GoogleAccessProtectedResource(
	            autorizzazione.getAccessToken(),
	            Util.TRANSPORT,
	            Util.JSON_FACTORY,
	            Auth.CLIENT_ID,
	            Auth.CLIENT_SECRET,
	            autorizzazione.getRefreshToken());
	    
	    Plus plusdatornare = new Plus(Util.TRANSPORT, requestInitializer, Util.JSON_FACTORY);
	   
	    return plusdatornare;
	  }
  
  /**
   * Get the profile for the authenticated user.
   *
   * @throws IOException if unable to call API
   */
  @SuppressWarnings("unused")
private static void getProfile() throws IOException {
    header("Get my Google+ profile");
    try {
    Person profile = plus.people().get("me").execute();
      show(profile);
    } catch (HttpResponseException e) {
      log.severe(Util.extractError(e));
      throw e;
    }
  }

  /**
   * Print the specified person on the command line.
   *
   * @param person the person to show
   */
  public static void show(Person person) {
    System.out.println("id: " + person.getId());
    System.out.println("name: " + person.getDisplayName());
    System.out.println("image url: " + person.getImage().getUrl());
    System.out.println("profile url: " + person.getUrl());
  }

  /**
   * Print the specified activity on the command line.
   *
   * @param activity the activity to show
   */
  @SuppressWarnings("unused")
  private static void show(Activity activity) {
    System.out.println("id: " + activity.getId());
    System.out.println("url: " + activity.getUrl());
    System.out.println("content: " + activity.getPlusObject().getContent());
  }

  private static void header(String name) {
    System.out.println();
    System.out.println("============== " + name + " ==============");
    System.out.println();
  }
  
}