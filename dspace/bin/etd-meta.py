#!/usr/bin/env python3
#==============================================================================
#
#         FILE:  etd-meta.py
#        USAGE:  ./etd-meta.py [path/to/input/*.xml] > [output.csv]
#  DESCRIPTION:  Extract metadata necessary for creation of a SAF batch
#                via the SAF builder and write the data to a CSV file.
#       AUTHOR:  Joshua A. Westgard
#      CREATED:  2016.04.14
#      VERSION:  1
#
#==============================================================================
import csv
import lxml.etree as ET
import sys

# map XML to CSV in form: {'xml element': 'target column'}
CSVMAP = {
    'PubNumber': 'filename',
    'AuthorFullName': 'dc.contributor.author',
    'DegreeYear': 'dc.date.issued',
    'DissLanguage': 'dc.language.iso',
#    'SubjectGroupDesc': 'dc.subject',
    'SubjectDesc': 'dc.subject',
#    'Keyword': 'dc.subject',
    'Title': 'dc.title',
    'DegreeCode': 'dc.type'
    }


def extract_metadata(file):
    '''Process an XML file, extracting fields and mapping data
    according to the relationships specified in the CSVMAP'''
    print('Processing {0} ...'.format(file), file=sys.stderr)
    tree = ET.parse(file)
    root = tree.getroot()
    ETDmeta = {
        'dc.contributor.publisher': [
            'Digital Repository at the University of Maryland',
            'University of Maryland (College Park, Md)'
            ]
        }
    # iterate over the XML tree
    for elem in root.iter():
        # pull vales from nodes that match the CSVMAP
        for target_elem, csv_col in CSVMAP.items():
            if elem.tag == target_elem:
                elem.text = apply_filtering(elem.tag, elem.text)
                if csv_col in ETDmeta:
                    ETDmeta[csv_col].append(elem.text)
                else:
                    ETDmeta[csv_col] = [elem.text]
    return ETDmeta


def apply_filtering(tag, text):
    """Alter certain metadata elements according to the specified rules."""
    if tag == 'PubNumber':
        return text + '.pdf'
    elif tag == 'Keyword':
        return text.title()
    elif tag == 'DegreeCode':
        if text == "Ph.D.":
            return "Dissertation"
    else:
        return text


def main(files):
    """Loop over files passed as arguments. When all files are
    processed, write the assembled data to a CSV file."""
    output = []

    # loop over the files received as arguments
    for f in files:
        metadata = extract_metadata(f)
        # convert values in metadata dictionary from lists to delimited strings
        output.append({k: "||".join(v) for (k,v) in metadata.items()})

    # write the data out as CSV
    fieldnames = list(set(CSVMAP.values()))
    fieldnames.append('dc.contributor.publisher')
    dw = csv.DictWriter(sys.stdout, fieldnames=fieldnames)
    dw.writeheader()
    for row in output:
        dw.writerow(row)


if __name__ == "__main__":
    main(sys.argv[1:])
