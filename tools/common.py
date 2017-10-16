import os
import semver
import yaml
import json
import sys
import re

from pyjavaproperties import Properties

WARN_EXIT_STATUS = int(os.environ['WARN_EXIT_STATUS']) if 'WARN_EXIT_STATUS' in os.environ else 113
global exitCode
exitCode = 0


class BuildConfig(object):
    def __init__(self, prerelease, publish, tag):
        super(BuildConfig, self).__init__()
        self.prerelease = prerelease
        self.publish = publish
        self.tag = tag


def loadProject(path=None):
    if path is None:
        projectFiles = ['project.yaml', 'project.yml', 'project.properties', 'package.json', 'gradle.properties']
        for p in os.listdir(os.getcwd()):
            if os.path.isfile(p) and p in projectFiles:
                path = p
                break
        if path is None:
            raise Exception('Expected one of %s to be present in %s' % (projectFiles, os.getcwd()))

    with open(path, 'r') as f:
        if path.endswith('.properties'):
            javaProps = Properties()
            javaProps.load(f)
            project = {}
            for key, value in javaProps.items():
                keyPath = key.split('.')
                x = project
                for section in keyPath[0:-1:]:
                    x = x.setdefault(section, {})
                x[keyPath[-1]] = value
        elif path.endswith('.yml') or path.endswith('.yaml'):
            project = yaml.load(f)
        elif path.endswith('.json'):
            project = json.load(f)
            if 'cfl' in project:
                project.update(project.pop('cfl', None))
        else:
            raise Exception('Unknown project definition type')
    for requiredPath in [['name'], ['version'], ['group', 'name']]:
        x = project
        for pathSegment in requiredPath:
            if pathSegment not in x:
                print project
                raise Exception('Expected %s to be specified in project props' % ('.'.join(requiredPath)))
            x = project[pathSegment]
    return project


def updateVersion(project, prerelease):
    print '## Updating verion'

    versionFile = 'build.version'
    if os.path.isfile(versionFile):
        with open(versionFile, 'r') as f:
            version = f.read().strip()
        print 'Read existing build version (%s) from file (%s).' % (version, versionFile)
    else:
        base = semver.parse(project['version'])
        version = semver.format_version(base['major'], base['minor'], base['patch'], prerelease)

        with open(versionFile, 'w') as f:
            f.write(version)
        print 'Writing build version (%s) to file (%s)' % (version, versionFile)

    # Check version is a valid semantic version.
    semver.parse(version)
    project['version'] = version

    print 'Using version %s.' % version
    print


def loadConfig():
    parsed = {}
    if 'CI' in os.environ:
        developBranch = os.environ.get('DEVELOP_BRANCH', 'develop')
        buildTag = os.environ.get('CI_BUILD_TAG', None)
        buildRefName = os.environ['CI_BUILD_REF_NAME']
        publish = True

        if buildTag is not None:
            prerelease = None
            tag = 'latest'
        elif buildRefName == developBranch:
            prerelease = 'dev.%s' % os.environ['CI_PIPELINE_ID']
            tag = 'dev'
        elif buildRefName.startswith('hotfix') or buildRefName.startswith('release'):
            prerelease = 'rc.%s' % os.environ['CI_PIPELINE_ID']
            tag = 'next'
        else:
            parts = re.split(r'[\/\-\.]', buildRefName)
            cleanParts = map(lambda x: re.sub(r'[^a-zA-Z0-9]+', '', x), parts)
            cleanBuildRef = '.'.join(cleanParts)
            prerelease = 'ci.%s.%s' % (cleanBuildRef, os.environ['CI_PIPELINE_ID'])
            tag = 'local'
            publish = False

        if 'publish' in os.environ:
            publish = os.environ['publish'].lower() == 'true'

        return BuildConfig(prerelease, publish, tag)
    else:
        prerelease = os.environ['prerelease'] if 'prerelease' in os.environ else 'local'
        return BuildConfig(prerelease, os.environ.get('publish', 'false').lower() == 'true', os.environ.get('tag', 'local'))
    return parsed


def checkExitCode(code):
    global exitCode
    if code == WARN_EXIT_STATUS:
        print 'WARN: Program exited with %d (special WARN exit code)' % WARN_EXIT_STATUS
        exitCode = code
    elif code != 0:
        print 'ERROR: Program exited with %d' % code
        sys.exit(code)
    return code


def exit():
    global exitCode
    sys.exit(exitCode)
