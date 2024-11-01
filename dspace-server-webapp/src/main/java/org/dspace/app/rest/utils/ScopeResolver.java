/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.utils;

import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.dspace.content.service.CollectionService;
import org.dspace.content.service.CommunityService;
import org.dspace.core.Context;
import org.dspace.discovery.IndexableObject;
import org.dspace.discovery.indexobject.IndexableCollection;
import org.dspace.discovery.indexobject.IndexableCommunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility bean that can resolve a scope in the REST API to a DSpace Object
 */
@Component
public class ScopeResolver {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(ScopeResolver.class);

    @Autowired
    CollectionService collectionService;

    @Autowired
    CommunityService communityService;

    // UMD Customization
    // This change has been provided to DSpace in Pull Request 9482
    // https://github.com/DSpace/DSpace/pull/9482
    // This customization marker can be remove when MD-SOAR is update to
    // a DSpace version containing this change.
    /**
     * Returns an IndexableObject corresponding to the community or collection
     * of the given scope, or null if the scope is not a valid UUID, or is a
     * valid UUID that does not correspond to a community of collection.
     *
     * @param context the DSpace context
     * @param scope a String containing the UUID of the community or collection
     * to return.
     * @return an IndexableObject corresponding to the community or collection
     * of the given scope, or null if the scope is not a valid UUID, or is a
     * valid UUID that does not correspond to a community of collection.
     */
    // End UMD Customization
    public IndexableObject resolveScope(Context context, String scope) {
        IndexableObject scopeObj = null;
        if (StringUtils.isNotBlank(scope)) {
            try {
                UUID uuid = UUID.fromString(scope);
                scopeObj = new IndexableCommunity(communityService.find(context, uuid));
                if (scopeObj.getIndexedObject() == null) {
                    scopeObj = new IndexableCollection(collectionService.find(context, uuid));
                    // UMD Customization
                    // This change has been provided to DSpace in Pull Request 9482
                    // https://github.com/DSpace/DSpace/pull/9482
                    // This customization marker can be remove when MD-SOAR is update to
                    // a DSpace version containing this change.
                    if (scopeObj.getIndexedObject() == null) {
                        // Can't find the UUID as a community or collection
                        // so log and return null
                        log.warn(
                            "The given scope string " +
                            StringUtils.trimToEmpty(scope) +
                            " is not a collection or community UUID."
                        );
                        scopeObj = null;
                    }
                    // End UMD Customization
                }
            } catch (IllegalArgumentException ex) {
                log.warn("The given scope string " + StringUtils.trimToEmpty(scope) + " is not a UUID", ex);
            } catch (SQLException ex) {
                log.warn(
                    "Unable to retrieve DSpace Object with ID " + StringUtils.trimToEmpty(scope) + " from the database",
                    ex);
            }
        }

        return scopeObj;
    }

}
