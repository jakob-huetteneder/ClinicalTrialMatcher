from xml.dom import minidom
import os
import sys
import json
import re

def main():

    # Get the file path from the command line
    file_path = 'patients/admission_notes.xml'

    # Open the file and parse it
    xmldoc = minidom.parse(file_path)

    # Get the admission notes
    admission_notes = xmldoc.getElementsByTagName('topic')
    
    # Get content of each tag
    admission_notes = [admission_note.firstChild.data.strip() for admission_note in admission_notes]

        

    # Write the output to file
    with open('admission_notes.json', 'w') as f:
        json.dump(admission_notes, f, indent=2)


if __name__ == "__main__":
    main()