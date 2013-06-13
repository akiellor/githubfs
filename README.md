GithubFS
========

Expose the github api as a file system.

This basic implementation lists issues as files with their body as content.

To Try It.
----------
0. git clone git@github.com:akiellor/githubfs.git
1. Install either OSXFuse (OSX) or libfuse (Linux)
2. gradle distZip
3. unzip build/distributions/githubfs.zip -d .
4. ./githubfs/bin/githubfs <your-mountpoint>
5. ls <your-mountpoint>

