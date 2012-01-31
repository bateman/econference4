package it.uniba.di.cdg.xcore.ui.formatter;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;


public class EmailFormatter implements FormatListener {

    @Override
    public List<StyleRange> applyFormatting( String text, int startOffset ) {

        List<StyleRange> list = new ArrayList<StyleRange>();
        final String[] tokens = text.split( " " );
        int i;
        String token;

        for (i = 0; i < tokens.length; i++) {
            token = tokens[i];

            if (!isValidEmailAddress( token ))
                continue;

            String mail = token;
            StyleRange style = new StyleRange();

            style.start = startOffset + text.indexOf( mail );
            style.length = mail.length();
            style.underline = true;
            style.underlineStyle = SWT.UNDERLINE_LINK;
            style.underlineColor = null;
            style.foreground = null;
            style.data = "mailto://" + mail;
            list.add( style );
        }
        return list;
    
    }

    /**
     * Returns true if the string is a valid email address
     * 
     * @param emailAddress
     * @return true if the string is an email address, false otherwise
     */
    private boolean isValidEmailAddress( String emailAddress ) {
        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile( expression, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher( inputStr );
        return matcher.matches();
    }

}
