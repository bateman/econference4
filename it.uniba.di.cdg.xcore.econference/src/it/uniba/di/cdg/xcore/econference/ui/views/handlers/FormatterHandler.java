package it.uniba.di.cdg.xcore.econference.ui.views.handlers;

import it.uniba.di.cdg.xcore.econference.ui.views.WhiteBoardViewUI;
import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;
import it.uniba.di.cdg.xcore.ui.views.TalkViewUI;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

public class FormatterHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute( ExecutionEvent event ) throws ExecutionException {
        // Get the ID for the command
        String command_id = event.getCommand().getId();
        // Get the ID for the PartView (WhiteBoardUI and TalkViewUI extend IPartview)
        String IPartViewID = HandlerUtil.getActivePart( event ).getSite().getId();
        //System.out.println(IPartViewID);
        // Retrieve the active page of the Workbench
        final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow( event ).getActivePage();
        final IViewPart view = page.findViewReference( IPartViewID ).getView( true );
        // Applies formatting style to the correct instance
        formatText( view, command_id );
        return null;
    }

    /**
     * This method applies formatting style to the correct instance
     * 
     * @param view
     *        the IViewPart of the Workbench
     * @param command_id
     *        the command id
     */
    private void formatText( IViewPart view, String command_id ) {
        // Check the right class for the IPartView instance
        if (view instanceof TalkViewUI) {
            final TalkViewUI tv = (TalkViewUI) view;
            // Applies formatting style
            tv.applyFormatting( getFormattingSymbol( command_id ) );
        }
        if (view instanceof WhiteBoardViewUI) {
            final WhiteBoardViewUI wb = (WhiteBoardViewUI) view;
            // Applies formatting style
            wb.applyFormatting( getFormattingSymbol( command_id ) );
        }
    }

    /**
     * Returns the correspondent symbol for a formatting style
     * 
     * @param command_id
     *        the command id
     * @return the corresponding symbol for the formatting style, null otherwise
     */
    private String getFormattingSymbol( String command_id ) {
      String symbol = null;
      if (command_id.contains( "bold" ))
          symbol = RichFormatting.BOLD_MARKER.getCode();
      if (command_id.contains( "italic" ))
          symbol = RichFormatting.ITALIC_MARKER.getCode();
      if (command_id.contains( "latex" ))
          symbol = RichFormatting.LATEX_MARKER.getCode();
      if (command_id.contains( "underline" ))
    	  symbol = RichFormatting.UNDERLINE_MARKER.getCode();
      if (command_id.contains( "strike" ))
    	  symbol = RichFormatting.STRIKEOUT_MARKER.getCode();
      return symbol;
  }
    
    

}
