package com.technoetic.xplanner.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Permission;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Setting;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.Integration;
import com.technoetic.xplanner.forms.FeatureEditorForm;
import com.technoetic.xplanner.forms.IterationEditorForm;
import com.technoetic.xplanner.forms.PersonEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.forms.TaskEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;
import xplanner.util.DomainUtils;


public class AuthorizerImpl implements Authorizer {
   private AuthorizerQueryHelper authorizerQueryHelper;
   private PrincipalSpecificPermissionHelper principalSpecificPermissionHelper;

   public AuthorizerImpl() {
   }

   //TODO resource should be a DomainObject
   public boolean hasPermission(int projectId, int personId, Object resource, String permission)
         throws AuthenticationException {
      int id;
      try {
         id = ((Integer) PropertyUtils.getProperty(resource, "id")).intValue();
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
      return hasPermission(projectId, personId, DomainUtils.getTypeOfResource(resource), id, permission);
   }


   public boolean hasPermission(int projectId,
                                int personId,
                                String resourceType,
                                int resourceId,
                                String permissionName) throws AuthenticationException {
      try {
         Map permissionsByProjectMap = principalSpecificPermissionHelper.getPermissionsForPrincipal(personId);
         return Permission.permissionMatches(permissionName,
                                  resourceType,
                                  resourceId,
                                  (List) permissionsByProjectMap.get(new Integer(projectId)))
                ||
                // For 0.7 only sysadmins have any project permissions
                 Permission.permissionMatches(permissionName,
                                  resourceType,
                                  resourceId,
                                  (List) permissionsByProjectMap.get(Project.ANY_PROJECT));
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
   }


   public Collection getPeopleWithPermissionOnProject(List allPeople, int projectId) throws AuthenticationException {
      Collection people = new ArrayList();
      for (int i = 0; i < allPeople.size(); i++) {
         Person person = (Person) allPeople.get(i);
         if (hasPermission(projectId, person.getId(), "system.project", projectId, "edit")) {
            people.add(person);
         }
      }
      return people;
   }

   public Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean includeWildcardProject)
         throws AuthenticationException {
      try {
         return authorizerQueryHelper.getRolesForPrincipalOnProject(principalId, projectId, includeWildcardProject);
      } catch (DataAccessException e) {
         throw new AuthenticationException(e);
      }
   }

   public boolean hasPermissionForSomeProject(int personId,
                                              String resourceType, int resourceId, String permission)
         throws AuthenticationException {
      try {
         List projects =
               authorizerQueryHelper.getAllUnhidenProjects();
         return hasPermissionForSomeProject(projects, personId, resourceType, resourceId, permission);
      } catch (AuthenticationException e) {
         throw e;
      } catch (Exception e) {
         throw new AuthenticationException(e);
      }
   }

   public boolean hasPermissionForSomeProject(Collection projects,
                                              int personId,
                                              String resourceType,
                                              int resourceId,
                                              String permission)
         throws AuthenticationException {
      for (Iterator iterator = projects.iterator(); iterator.hasNext();) {
         Project project = (Project) iterator.next();
         if (hasPermission(project.getId(), personId, resourceType, resourceId, permission)) {
            return true;
         }
      }
      return false;
   }

   public void setPrincipalSpecificPermissionHelper(PrincipalSpecificPermissionHelper principalSpecificPermissionHelper) {
      this.principalSpecificPermissionHelper = principalSpecificPermissionHelper;
   }

   public void setAuthorizerQueryHelper(AuthorizerQueryHelper authorizerQueryHelper) {
      this.authorizerQueryHelper = authorizerQueryHelper;
   }
}
