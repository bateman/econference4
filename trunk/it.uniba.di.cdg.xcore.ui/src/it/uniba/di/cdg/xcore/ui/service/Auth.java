/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * 
 * This file was originally distribuited by Google and 
 * modified for the econference project
 */
package it.uniba.di.cdg.xcore.ui.service;


import java.io.IOException;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.auth.oauth2.draft10.AuthorizationRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Implements OAuth authentication.
 *
 * @author Yaniv Inbar
 * @author Will Norris
 * @author Jenny Murphy
 */


/**
 * 
 * Modified version of the original OAuth authentication
 * The procedure has been splitted it in two different step,
 * particularly useful if you have to use it through a GUI
 * 
 * @author Francesco Malerba
 * 
 *
 */
public class Auth {

	
	/*
	 * 
	 * # === OAuth Information === #

# Register your application at: https://code.google.com/apis/console/
oauth_client_id = 193401750970.apps.googleusercontent.com
oauth_client_secret = aUsAMCDVe1wCLj1tHL6Jh-7h
google_api_key = AIzaSyBM5Fy_N7CWrw9QB4ypzudKBG9EevymGVY

# Space separated list of OAuth scopes your application will request access to
oauth_scopes = https://www.googleapis.com/auth/plus.me

# The URL for your application where a user should be redirected after granting
# access.  The special value "urn:ietf:wg:oauth:2.0:oob" means that the user
# will not be redirected back to your application, but rather will be given a
# code to enter into your application.
oauth_redirect_uri = urn:ietf:wg:oauth:2.0:oob

	 * 
	 * 
	 * */
  public static String CLIENT_ID = "1013420880173.apps.googleusercontent.com";

  /**
   * OAuth client secret.
   */
  public static String CLIENT_SECRET = "SXm0pd24ugk8H-wxl5ARGqYU";

  /**
   * OAuth client secret.
   */
  public static String GOOGLE_API_KEY = "AIzaSyDMoCPi857TLhqcINY8WUydTlVTLooxO4E";

  /**
   * OAuth redirect URI.
   */
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

  /**
   * Space separated list of OAuth scopes.
   */
  private  String SCOPES = "https://www.googleapis.com/auth/plus.me";

  private static  AccessTokenResponse accessTokenResponse;

  public  String getRefreshToken() {
    return accessTokenResponse.refreshToken;
  }

  public  String getAccessToken() {
	  if (accessTokenResponse != null)
    return accessTokenResponse.accessToken;
	  else 
	  {
    System.out.println("token null, ritorno vuoto");
	return "";
	  }
	  }

  /**
   * Send the user through the OAuth flow to authorize the application and 
   * send him on the browser to get the authorization token
   *
   * @throws IOException unable to complete OAuth flow
   */
  public  void askauthorize() throws IOException {
    if ("".equals(CLIENT_ID)) {
      System.err.println("Please specify your OAuth Client ID");
      System.exit(1);
    }

    // build the authorization URL
    AuthorizationRequestUrl authorizeUrl = new GoogleAuthorizationRequestUrl(
            CLIENT_ID,
            REDIRECT_URI,
            SCOPES
    );
    authorizeUrl.redirectUri = REDIRECT_URI;
    authorizeUrl.scope = SCOPES;
    String authorizationUrl = authorizeUrl.build();

    // launch in browser
    System.out.println("Attempting to open a web browser to start the OAuth2 flow");
    Util.openBrowser(authorizationUrl);

    // request code from user
    System.out.print("Now we need to get code from label");
    
  }
  
  /**
   * 
   * procedure to be used to have the authorization,
   * it needs the authorization code in input.
   * If catch an exception then the autorization fail and need to 
   * call askauth again.
   * @param code , the authorization code
   */
  public void authorize(String code) {
	// Exchange code for an access token
	    AccessTokenResponse accessTokenResponse;
		try {
			accessTokenResponse = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(
			        new NetHttpTransport(),
			        new GsonFactory(),
			        CLIENT_ID,
			        CLIENT_SECRET,
			        code,
			        REDIRECT_URI
			).execute();
			
		    Auth.accessTokenResponse = accessTokenResponse;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Autorizzazione fallita");
		}

	  
	  
	  
  }
  
  

}
