package com.naikvarun.lab.rcp.app.project;

import java.net.URI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class CustomProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage pageOne;

	public CustomProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		String projectName = pageOne.getProjectName();
		URI location = null;
		if (!pageOne.useDefaults()) {
			location = pageOne.getLocationURI();
		}

		CustomProjectCreater projectCreator = new CustomProjectCreater(null);

		return projectCreator.createProject(projectName, location);
	}
	
	@Override
	public void addPages() {
		super.addPages();
		pageOne = new WizardNewProjectCreationPage("Custom Project");
		pageOne.setTitle("Custom Project Creation Title");
		pageOne.setDescription("Custom Project Creation Description");
		addPage(pageOne);

	}

}
