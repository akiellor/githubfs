GithubFS
========

Expose the github api as a file system.

This basic implementation lists issues as files with their body as content.

To Try It.
----------
0. git clone git@github.com:akiellor/githubfs.git
1. Install either [OSXFuse][osxfuse] (OSX) or libfuse (Linux)
2. gradle distZip
3. unzip build/distributions/githubfs.zip -d .
4. get your oauth key (`token` field in response): 
`curl -u 'your-username' -d '{"scopes":["repo"],"note":"Help example"}' https://api.github.com/authorizations`
5. create a directory to use as a mountpoint
6. ./githubfs/bin/githubfs **your-username** **your-oauthkey** akiellor/githubfs **your-mountpoint**
7. ls **your-mountpoint**

[osxfuse]: http://osxfuse.github.io/
