/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.matcher;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasNoJsonPath;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;

/**
 * Helper class to simplify testing of the submission form configuration
 *
 * @author Andrea Bollini (andrea.bollini at 4science.it)
 *
 */
public class SubmissionFormFieldMatcher {

    private SubmissionFormFieldMatcher() {
    }

    /**
     * Shortcut for the
     * {@link SubmissionFormFieldMatcher#matchFormFieldDefinition(String, String, String, boolean, String, String, String)}
     * with a null style
     *
     * @param type
     *            the expected input type
     * @param label
     *            the expected label
     * @param mandatoryMessage
     *            the expected mandatoryMessage, can be null. If not empty the fiedl is expected to be flagged as
     *            mandatory
     * @param repeatable
     *            the expected repeatable flag
     * @param hints
     *            the expected hints message
     * @param metadata
     *            the expected metadata
     * @return a Matcher for all the condition above
     */
    public static Matcher<? super Object> matchFormFieldDefinition(String type, String label, String mandatoryMessage,
                                                                   boolean repeatable, String hints, String metadata) {
        return matchFormFieldDefinition(type, label, mandatoryMessage, repeatable, hints, null, metadata);
    }

    /**
     * Check the json representation of a submission form
     *
     * @param type
     *            the expected input type
     * @param label
     *            the expected label
     * @param mandatoryMessage
     *            the expected mandatoryMessage, can be null. If not empty the field is expected to be flagged as
     *            mandatory
     * @param repeatable
     *            the expected repeatable flag
     * @param hints
     *            the expected hints message
     * @param style
     *            the expected style for the field, can be null. If null the corresponding json path is expected to be
     *            missing
     * @param metadata
     *            the expected metadata
     * @return a Matcher for all the condition above
     */
    public static Matcher<? super Object> matchFormFieldDefinition(String type, String label, String mandatoryMessage,
                                                                   boolean repeatable, String hints, String style,
                                                                   String metadata) {
        return allOf(
            // check each field definition
            hasJsonPath("$.input.type", is(type)),
            hasJsonPath("$.label", containsString(label)),
            hasJsonPath("$.selectableMetadata[0].metadata", is(metadata)),
            mandatoryMessage != null ? hasJsonPath("$.mandatoryMessage", containsString(mandatoryMessage)) :
                hasNoJsonPath("$.mandatoryMessage"),
            hasJsonPath("$.mandatory", is(mandatoryMessage != null)),
            hasJsonPath("$.repeatable", is(repeatable)),
            style != null ? hasJsonPath("$.style", is(style)) :
                hasNoJsonPath("$.style"),
            hasJsonPath("$.hints", containsString(hints))
        );
    }

    /**
     * Check the json representation of an open relationship field.
     * This is a combination of an entity relationship lookup and a plain text metadata entry field
     *
     * @param type
     *            the expected input type
     * @param label
     *            the expected label
     * @param mandatoryMessage
     *            the expected mandatoryMessage, can be null. If not empty the field is expected to be flagged as
     *            mandatory
     * @param repeatable
     *            the expected repeatable flag
     * @param hints
     *            the expected hints message
     * @param metadata
     *            the expected metadata
     * @param relationshipType
     *            the type of relationship
     * @param filter
     *            the optional filter to be used for the lookup
     * @param searchConfiguration
     *            the searchConfiguration to be used for the lookup
     * @return a Matcher for all the condition above
     */
    public static Matcher<? super Object> matchFormOpenRelationshipFieldDefinition(String type, String label,
                                                                                   String mandatoryMessage,
                                                                                   boolean repeatable, String hints,
                                                                                   String metadata,
                                                                                   String relationshipType,
                                                                                   String filter,
                                                                                   String searchConfiguration) {
        return allOf(
            hasJsonPath("$.selectableRelationship.relationshipType", is(relationshipType)),
            hasJsonPath("$.selectableRelationship.filter", is(filter)),
            hasJsonPath("$.selectableRelationship.searchConfiguration", is(searchConfiguration)),
            matchFormFieldDefinition(type, label, mandatoryMessage, repeatable, hints, metadata));
    }

    /**
     * Check the json representation of a closed relationship field.
     * This is an entity relationship lookup without a plain text metadata entry field
     *
     * @param label
     *            the expected label
     * @param mandatoryMessage
     *            the expected mandatoryMessage, can be null. If not empty the field is expected to be flagged as
     *            mandatory
     * @param repeatable
     *            the expected repeatable flag
     * @param hints
     *            the expected hints message
     * @param relationshipType
     *            the type of relationship
     * @param filter
     *            the optional filter to be used for the lookup
     * @param searchConfiguration
     *            the searchConfiguration to be used for the lookup
     * @return a Matcher for all the condition above
     */
    public static Matcher<? super Object> matchFormClosedRelationshipFieldDefinition(String label,
                                                                                     String mandatoryMessage,
                                                                                     boolean repeatable, String hints,
                                                                                     String relationshipType,
                                                                                     String filter,
                                                                                     String searchConfiguration) {
        return allOf(
            hasJsonPath("$.selectableRelationship.relationshipType", is(relationshipType)),
            hasJsonPath("$.selectableRelationship.filter", is(filter)),
            hasJsonPath("$.selectableRelationship.searchConfiguration", is(searchConfiguration)),
            hasJsonPath("$.label", is(label)),
            mandatoryMessage != null ? hasJsonPath("$.mandatoryMessage", containsString(mandatoryMessage)) :
                hasNoJsonPath("$.mandatoryMessage"),
            hasJsonPath("$.mandatory", is(mandatoryMessage != null)),
            hasJsonPath("$.repeatable", is(repeatable)),
            hasJsonPath("$.hints", containsString(hints)),
            hasNoJsonPath("$.input.type"),
            hasNoJsonPath("$.selectableMetadata"));
    }
}