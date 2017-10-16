#!/usr/bin/env python

import imp
import os

from subprocess import call
from pyjavaproperties import Properties


common = imp.load_source('common', os.path.join(os.path.dirname(__file__), 'common.py'))
checkExitCode = common.checkExitCode


def runBuildTasks(project):
    props = {
        'project.version': project['version']
    }
    return call(['ant', 'dist', '-Dproject.version=%s' % project['version']])


def writeVersion(project):
    fileName = 'project.properties'
    with open(fileName, 'r') as f:
        p = Properties()
        p.load(f)

    p['version'] = project['version']

    with open(fileName, 'w') as f:
        p.store(f)


if __name__ == '__main__':
    config = common.loadConfig()
    project = common.loadProject()
    common.updateVersion(project, config.prerelease)
    writeVersion(project)
    checkExitCode(runBuildTasks(project))
    common.exit()
