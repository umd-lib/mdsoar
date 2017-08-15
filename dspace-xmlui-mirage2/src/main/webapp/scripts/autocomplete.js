/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */ 

// Self invoking function to add auto-suggest to submission form fields.
$(function() {

	// Calling solr suggest functionality using discovery url. 
	var suggest_url = "/JSON/discovery/suggest?";

	//data fields names to add auto-suggest functionality.
	addSuggester("dc_genre", "formatSuggest");
	addSuggester("dc_subject", "subjectSuggest");
	
	//function to make call to solr suggest by building required url on keyup event.
	function addSuggester(dc_field, suggest_component) {
		var full_field_id = "#aspect_submission_StepTransformer_field_" + dc_field;
		$(full_field_id).keyup(function(){
			var suggest_query = $(full_field_id).val();
			$('#results').html(""); // Change the id of the suggestions div to autosuggest_results
			$.ajax({
	              type:"GET",
	              url: suggest_url,
	              data: "field=" + suggest_component + "&query=" + suggest_query,
	              success: function (response) {
	                  if (response != null && response != undefined) {
	                	  var unhighlighed_response = response.replace(/<\/?[^>]+>/gi, "");
	                      var data = JSON.parse(unhighlighed_response);
	                      var suggestion_response = data.suggest[suggest_component];
	                      var suggestions = suggestion_response[suggest_query]["suggestions"];
	                      var suggestion_terms = suggestions.map(function(suggestion) {return suggestion.term});
                          $(full_field_id).autocomplete({
	                            source: suggestion_terms  ,
	                            select: function(event,ui){
	                                 $(full_field_id).val(ui.item.label);
	                            }
                          })
	                  }
	              },
	              error: function(response) {
	                 console.log("Error while retrieving suggestions!");
	              }
	         })
		})
	}
	
  });