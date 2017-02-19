package com.naikvarun.lab.rcp.app;

import java.net.URL;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.osgi.framework.Bundle;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		IDE.registerAdapters();

		final String ICONS_PATH = "/icons/full/";
		final String PATH_OBJECT = ICONS_PATH + "obj16";

		Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
		declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT, PATH_OBJECT + "prj_obj.png",
				true);
		declareWorkbenchImage(configurer, ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED,
				PATH_OBJECT + "cprj_obj.png", true);

	}

	private void declareWorkbenchImage(IWorkbenchConfigurer configurer, Bundle bundle, String symbolicName, String path,
			boolean shared) {
		URL url = bundle.getEntry(path);
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
		configurer.declareImage(symbolicName, descriptor, shared);
	}

	@Override
	public boolean preShutdown() {
		try {
			ResourcesPlugin.getWorkspace().save(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return super.preShutdown();
	}

}
