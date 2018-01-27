# server



tomcat + spring 

...

git checkout -b <branch name> : branch 이동(없으면 생성해) 

git branch -v : branch 상태 확인 

git push origin <branch name> : 해당 branch로 push 

기능 추가가 완료되면 master로 merge.. 

master branch와 추가 branch간 히스토리가 다르다는 오류로 pull request가 안될때 

git rebase --onto master <branch 이름> : branch commit 히스토리를 master에 맞춰줌(?)... 

git cherry-pick <commit 히스토리..> : 새로운 branch의 최종 commit 번호를 확인하고 이동 

그 다음 commit 및 git push -f origin <branch 이름> 하면 해결되는데, 이 방법은 조금 많이 위험한 것 같다.. 


