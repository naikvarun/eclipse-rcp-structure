package com.naikvarun.lab.rcp.app.project;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class CustomProjectCreater {

	private static final String[] PROJECT_STRUCTURE = { "folder1", "folder1/sub-folder1", "folder1/sub-folder2",
			"folder2" };

	private IProgressMonitor monitor;

	public CustomProjectCreater(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public boolean createProject(String projectName, URI location) {
		Assert.isNotNull(projectName);
		Assert.isTrue(projectName.trim().length() > 0);

		IProject project = createBaseProject(projectName, location);
		try {
			addNatureToProject(project);

			addToProjectStructure(project, PROJECT_STRUCTURE);
		} catch (CoreException ex) {
			ex.printStackTrace();
			project = null;
		}

		return project != null;
	}

	private void addToProjectStructure(IProject project, String[] projectStructure) throws CoreException {
		for (String structure : projectStructure) {
			IFolder projectFolder = project.getFolder(structure);
			createFolder(projectFolder);
		}
	}

	private void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, monitor);
		}
	}

	private void addNatureToProject(IProject project) throws CoreException {
		if (!project.hasNature(CustomProjectNature.ID)) {
			IProjectDescription projectDescription = project.getDescription();
			String[] previousNatures = projectDescription.getNatureIds();
			String[] newNatures = new String[previousNatures.length + 1];
			System.arraycopy(previousNatures, 0, newNatures, 0, previousNatures.length);
			newNatures[previousNatures.length] = CustomProjectNature.ID;
			project.setDescription(projectDescription, monitor);
		}
	}

	private IProject createBaseProject(String projectName, URI location) {
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription projectDescription = newProject.getWorkspace().newProjectDescription(projectName);
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			projectDescription.setLocationURI(projectLocation);
			try {
				newProject.create(projectDescription, monitor);
				if (!newProject.isOpen()) {
					newProject.open(monitor);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}
}
