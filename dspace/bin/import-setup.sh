#!/usr/bin/env bash

#==============================================================================
#
#         FILE:  import-setup.sh
#        USAGE:  import-setup.sh [file] [handle]
#  DESCRIPTION:  Given a file or directory path and DSpace handle, set up the
#                directories and files needed for adding the files to the
#                existing object denoted by the handle using the dspace
#                itemupdate command.
#       AUTHOR:  Joshua A. Westgard
#      CREATED:  2017.11.01
#      VERSION:  2
#
#==============================================================================

FILEPATH=$1
HANDLE=$2
BATCHDIR="load$(date +%Y%m%d)"
DC="$BATCHDIR/item_1/dublin_core.xml"

if [[ -e "$BATCHDIR" ]]; then
    echo "Output directory exists. Exiting..."
    exit 1
fi

if [[ ! -e "$FILEPATH" ]]; then
    echo "File path does not exist. Exiting..."
    exit 1
fi

function move_file {
    ITEMPATH=$1
    mv "$ITEMPATH" "$BATCHDIR/item_1"
    basename "$ITEMPATH" >> "$BATCHDIR/item_1/contents"
}

#----------------------------------------------------------------------
# set up SAF package structure
#----------------------------------------------------------------------
echo "Creating SAF package to attach bitstreams to $HANDLE ..."
mkdir -p "$BATCHDIR/item_1" && echo "  - created output dirs;"
touch "$BATCHDIR/item_1/contents" && echo "  - created contents file;"
echo $HANDLE >> "$BATCHDIR/item_1/handle" && echo "  - created handle file;"

#----------------------------------------------------------------------
# move file or files into position inside SAF directories
#----------------------------------------------------------------------
echo "Checking $FILEPATH ..."
if [[ -f "$FILEPATH" ]]; then
    echo "  - $FILEPATH is a single file; moving it:"
    move_file "$FILEPATH" && echo "    File moved."
elif [[ -d "$FILEPATH" ]]; then
    echo "  - $FILEPATH is a directory; moving files:"
    count=1
    for item in $(find "$FILEPATH" -type f); do
        if [[ -f $item ]]; then
            move_file "$item" && echo "    $count. Moving $item"
            (( count += 1 ))
        fi
    done
fi

#----------------------------------------------------------------------
# create minimal dublin_core.xml containing URL of obj to be updated
#----------------------------------------------------------------------
echo "Creating dublin_core.xml ..."
cat >"$DC" <<END
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<dublin_core schema="dc">
  <dcvalue element="identifier" qualifier="uri">$HANDLE</dcvalue>
</dublin_core>
END
echo "Done."
