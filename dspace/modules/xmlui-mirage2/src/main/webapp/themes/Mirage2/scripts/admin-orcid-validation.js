/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

// Admin Item Edit - Add Metdata ORCID validation.
$(function () {
  $(document).ready(function () {
    var md_select = $('#aspect_administrative_item_EditItemMetadataForm_field_field');
    var md_value = $('#aspect_administrative_item_EditItemMetadataForm_field_value');
    md_select.change(function () {
      var selectedText = $(this).find("option:selected").text();
      console.log('Selected text: ' + selectedText);
      var orcid_md_value_text = $('#aspect_administrative_item_EditItemMetadataForm_field_value_text');
      if (selectedText == "dcterms.creator") {
        md_value.prop('disabled', true);
        md_value.hide();
        if (orcid_md_value_text.length == 0) {
          orcid_md_value_text = $('<input>').attr({
            id: 'aspect_administrative_item_EditItemMetadataForm_field_value_text',
            name: md_value.attr('name'),
            maxlength: 37,
            class: 'ds-text-field form-control',
            size: 37,
            pattern: 'https://orcid.org/\\d{4}-\\d{4}-\\d{4}-\\d{3}[X0-9]',
            oninvalid: "setCustomValidity('Must be of format https://orcid.org/xxxx-xxxx-xxxx-xxxx');",
            oninput: "setCustomValidity('');"
          });
          orcid_md_value_text.insertAfter(md_value)
        } else {
          orcid_md_value_text.show()
          orcid_md_value_text.prop('disabled', false);
        }
      } else {
        orcid_md_value_text.hide()
        orcid_md_value_text.prop('disabled', true);
        md_value.show();
        md_value.prop('disabled', false);
      }
    })
  });
})