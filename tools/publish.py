#!/usr/bin/env python

import imp
import os

from subprocess import call

common = imp.load_source('common', os.path.join(os.path.dirname(__file__), 'common.py'))
checkExitCode = common.checkExitCode

SCRIPTS_DIR = os.path.dirname(__file__)


def getIvyStatus(config):
    if config.tag == 'latest':
        return 'release'
    if config.tag == 'next':
        return 'milestone'
    else:
        return 'integration'


def runPublishTasks(config, project):
    return call([
        'ant',
        '-f', os.path.join(SCRIPTS_DIR, 'ant-publish.xml'),
        '-Divy.settings.file=%s' % '/app/scripts/ivysettings.xml',
        '-Divy.status=%s' % getIvyStatus(config),
        '-Dbasedir=%s' % os.path.join(SCRIPTS_DIR, '..'),
        '-Divy.deliver.revision=%s' % project['version'],
        'publish',
    ])


if __name__ == '__main__':
    config = common.loadConfig()
    project = common.loadProject()
    checkExitCode(runPublishTasks(config, project))
    common.exit()
