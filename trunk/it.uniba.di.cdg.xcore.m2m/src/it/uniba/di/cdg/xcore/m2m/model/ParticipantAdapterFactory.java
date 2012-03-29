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
package it.uniba.di.cdg.xcore.m2m.model;

import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.IActivatableView;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Adapter for {@see it.uniba.di.cdg.xcore.m2m.model.IParticipant}: this will
 * provide a way for the framework to wire context actions to our model.
 * <p>
 * Note that the action filter currently accept several test properties:
 * <ul>
 * <li><b>status</b>, for testing the current online status
 * {@link IParticipant.Status})</li>
 * <li><b>role</b>, for testing the role of selected participant ({@see
 * it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role})</li>
 * <li><b>myRole</b>, for testing the role of the current local user ({@see
 * it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role})</li>
 * <li><b>myStatus</b>, for testing the status of the current local user ({@see
 * it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status})</li>
 * <li><b>specialRole</b>, for testing the special role of the user (this is a
 * String implementation-dependent, non an enum)</li>
 * <li><b>viewIsReadOnly</b>, for testing if a specific view is read-only</li>
 * <li><b>activePerspective</b>, for testing the active perspective ID</li>
 * </ul>
 */
public class ParticipantAdapterFactory implements IAdapterFactory {

	/**
	 * Participants adapter.
	 */
	private final IWorkbenchAdapter participantAdapter = new IWorkbenchAdapter() {
		public Object[] getChildren(Object o) {
			return new Object[0]; // No child
		}

		public ImageDescriptor getImageDescriptor(Object object) {
			IParticipant participant = (IParticipant) object;
			Image im1 = null, im2 = null;

			if (Role.MODERATOR.equals(participant.getRole())) {
				if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
						&& participant
								.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResources.ICON_MODERATOR_SCRIBE_VOTER)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)) {
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResources.ICON_MODERATOR_SCRIBE)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResources.ICON_MODERATOR_VOTER)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				}
				im1 = UiPlugin
						.getDefault()
						.getImageDescriptor(IImageResources.ICON_USER_MODERATOR)
						.createImage();
				im2 = getFlag(participant);
				return concatImage(im1, im2);
			} else if (Status.JOINED.equals(participant.getStatus())) {
				if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
						&& participant
								.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
					im1 = UiPlugin
							.getDefault()
							.getImageDescriptor(
									IImageResources.ICON_SCRIBE_VOTER)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)) {
					im1 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.ICON_SCRIBE)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				} else if (participant
						.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)) {
					im1 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.ICON_VOTER)
							.createImage();
					im2 = getFlag(participant);
					return concatImage(im1, im2);
				}
				im1 = UiPlugin.getDefault()
						.getImageDescriptor(IImageResources.ICON_USER_ACTIVE)
						.createImage();
				im2 = getFlag(participant);
				return concatImage(im1, im2);
			} else if (Status.FROZEN.equals(participant.getStatus()))
				return UiPlugin.getDefault().getImageDescriptor(
						IImageResources.ICON_USER_FROZEN);
			else
				// NOT_JOINED otherwise
				return UiPlugin.getDefault().getImageDescriptor(
						IImageResources.ICON_USER_OFFLINE);
		}

		public Image getFlag(IParticipant participant) {
			UserLanguages lang = UserLanguages.getInstance();
			HashMap<String, String> languages = lang.get_languages();
			Image im2 = null;

			if ((languages.containsKey(participant.getId()))
					&& (languages.get(participant.getId()) != null)) {
				im2 = new Image(null, 22, 10);
				String value = languages.get(participant.getId());
				if (value.equalsIgnoreCase("ar")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_AR)
							.createImage();
				} else if (value.equalsIgnoreCase("bn")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_BN)
							.createImage();
				} else if (value.equalsIgnoreCase("en")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_EN)
							.createImage();
				} else if (value.equalsIgnoreCase("es")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_ES)
							.createImage();
				} else if (value.equalsIgnoreCase("fr")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_FR)
							.createImage();
				} else if (value.equalsIgnoreCase("de")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_GE)
							.createImage();
				} else if (value.equalsIgnoreCase("hi")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_HI)
							.createImage();
				} else if (value.equalsIgnoreCase("it")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_IT)
							.createImage();
				} else if (value.equalsIgnoreCase("ja")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_JA)
							.createImage();
				} else if (value.equalsIgnoreCase("pl")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_PL)
							.createImage();
				} else if (value.equalsIgnoreCase("pt")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_PT)
							.createImage();
				} else if (value.equalsIgnoreCase("ru")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_RU)
							.createImage();
				} else if (value.equalsIgnoreCase("th")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_TH)
							.createImage();
				} else if (value.equalsIgnoreCase("tr")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_TR)
							.createImage();
				} else if (value.equalsIgnoreCase("zh")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_ZH)
							.createImage();
				} else if (value.equalsIgnoreCase("ko")) {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_KO)
							.createImage();
				} else {
					im2 = UiPlugin.getDefault()
							.getImageDescriptor(IImageResources.FLAG_DEF)
							.createImage();
				}
			}
			return im2;

		}

		public ImageDescriptor concatImage(Image im1, Image im2) {
			if (im2 == null) {
				return ImageDescriptor.createFromImage(im1);
			} else {
				Image bigImage = new Image(null, im1.getImageData().width
						+ im2.getImageData().width, im1.getImageData().height);
				GC gc = new GC(bigImage);
				gc.drawImage(im1, 0, 0);
				gc.drawImage(im2, im1.getImageData().width, 0);
				gc.dispose();
				return ImageDescriptor.createFromImage(bigImage);
			}
		}

		public String getLabel(Object o) {
			IParticipant p = (IParticipant) o;
			if (p.getPersonalStatus() != null
					&& !p.getPersonalStatus().equals(""))
				return String.format("%s (%s)", p.getNickName(),
						p.getPersonalStatus());
			else
				return p.getNickName();
		}

		public Object getParent(Object o) {
			return null;
		}
	};

	/**
	 * This is needed by the UI for filtering actions based on the property of
	 * the participant.
	 */
	private final IActionFilter contextMenuActionFilter = new IActionFilter() {
		public boolean testAttribute(Object target, String name, String value) {
			IParticipant p = (IParticipant) target;

			// System.out.println( String.format( "*** name == %s, value == %s",
			// name, value ) );

			if ("status".equals(name))
				return value.equals(p.getStatus().toString());
			// Discriminate upon special role
			else if ("specialRole".equals(name))
				return p.hasSpecialPrivilege(value);
			else if ("role".equals(name)) {
				// System.out.println( String.format( "Role of %s is %s",
				// p.getId(), p.getRole() ) );
				return value.equals(p.getRole().toString());
			} else if ("service".equals(name)) {
				ICapabilities capabilities = NetworkPlugin.getDefault()
						.getHelper().getRoster().getBackend().getCapabilities();
				for (Iterator<ICapability> iterator = capabilities.iterator(); iterator
						.hasNext();) {
					ICapability capability = iterator.next();
					if (capability.getName().equals(value))
						return true;
				}
				return false;
			} else if ("myRole".equals(name)) {
				IParticipant thisLocalUser = p.getChatRoom().getLocalUser();
				return thisLocalUser.getRole().equals(Role.valueOf(value));
			} else if ("myStatus".equals(name)) {
				IParticipant thisLocalUser = p.getChatRoom().getLocalUser();
				return thisLocalUser.getStatus().equals(Status.valueOf(value));
			} else if ("viewIsReadOnly".equals(name)) {
				final String viewId = value;
				IViewPart view = findView(viewId);

				boolean result = false;
				if (view instanceof IActivatableView) {
					result = ((IActivatableView) view).isReadOnly();
				}
				return result;
			} else if ("activePerspective".equals(name)) {
				IWorkbenchWindow activeWorkbenchWindow = PlatformUI
						.getWorkbench().getActiveWorkbenchWindow();
				if (activeWorkbenchWindow == null)
					return false;

				IWorkbenchPage activePage = activeWorkbenchWindow
						.getActivePage();
				if (activePage == null)
					return false;

				IPerspectiveDescriptor activePerspective = activePage
						.getPerspective();
				if (activePerspective == null)
					return false;

				return activePerspective.getId().equals(value);
			}

			// try {
			// final Field f = IParticipant.class.getDeclaredField( name );
			// final Object fValue = f.get( p );
			// return value.equals( fValue );
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			return false;
		}
	};

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		// System.out.println( String.format(
		// "ParticipantAdapterFactory.getAdapter( %s, %s )", adaptableObject,
		// adapterType ) );
		if (!(adaptableObject instanceof IParticipant))
			return null;

		if (adapterType == IWorkbenchAdapter.class)
			return participantAdapter;
		else if (adapterType == IActionFilter.class)
			return contextMenuActionFilter;

		// No adapter otherwise
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return new Class[] { IWorkbenchAdapter.class, IActionFilter.class };
	}

	private IViewPart findView(String viewId) {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(viewId);
	}
}
