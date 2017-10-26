#!/usr/bin/python

import os.path

BOILERPLATE = open('BOILERPLATE', 'r').read()

def apply_boilerplate(_, dirname, fnames):
    """ Suitable for calling from os.path.walk. """
    for _, name in list(enumerate(fnames)):
        if name.endswith('.java'):
            path = os.path.join(dirname, name)
            ensure_has_boilerplate(path)

def ensure_has_boilerplate(path):
    content = open(path, 'r').read()
    if content.find(BOILERPLATE.strip()) == -1:
        content = BOILERPLATE + content
        with open(path, 'w') as fout:
            fout.write(content)

if __name__ == '__main__':
    for directory in ('src'):
        os.path.walk(directory, apply_boilerplate, None)
