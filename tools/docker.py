#!/usr/bin/env python

import sys
import shutil
import imp
import os
import semver

from subprocess import call
from tempfile import mkdtemp

common = imp.load_source('common', os.path.join(os.path.dirname(__file__), 'common.py'))
checkExitCode = common.checkExitCode

BUILD_DOCKERFILE_IMAGE_NAME = 'artifacts.int.corefiling.com:5000/nimbus-tools/build-dockerfile:0.4.0'
SCRIPTS_DIR = os.path.dirname(__file__)


def updateDockerfile(srcPath, destPath, newImage=None):
    print '## Generating Dockerfile'
    with open(srcPath, 'r') as src:
        srcLines = src.readlines()
        with open(destPath, 'w') as dest:
            for line in srcLines:
                if line.strip().startswith('FROM') and newImage is not None:
                    line = 'FROM %s\n' % newImage
                sys.stdout.write(line)
                dest.write(line)
    print '## Finished writing Dockerfile'


def buildDockerImage(project, tag, customImage=None):
    dockerfile = os.path.join(SCRIPTS_DIR, 'Dockerfile.run-ivy')

    try:
        updateDockerfile(os.path.join(SCRIPTS_DIR, 'Dockerfile.run-ivy'), 'Dockerfile', customImage)

        env = os.environ.copy()
        prerelease = semver.parse(project['version'])['prerelease']
        if prerelease is not None:
            env['prerelease'] = prerelease
        env['publish'] = str(True).lower()
        env['tag'] = tag

        cmd = [
            '/app/scripts/build.py',
        ]
        buildArgs = {
            'org': project['group']['mavenId'],
            'module': project['name'],
            'revision': project['version'],
            'class': project['java']['main'],
        }
        if len(buildArgs) > 0:
            cmd.append('--')
            for key, value in buildArgs.items():
                cmd.extend(['--build-arg', '%s=%s' % (key, value)])

        return call(cmd, env=env)
    finally:
        os.remove('Dockerfile')

if __name__ == '__main__':
    config = common.loadConfig()
    project = common.loadProject()
    buildDockerImage(project, config.tag, project.get('java', {}).get('run', None))
    common.exit()
