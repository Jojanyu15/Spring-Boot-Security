INSERT INTO USERS(username,password,enabled)
values('user','test','true')

INSERT INTO USERS(username,password,enabled)
values('admin','test','true')

INSERT INTO AUTHORITIES(username,authority)
values('user','ROLE_USER')

INSERT INTO AUTHORITIES(username,authority)
values('admin','ROLE_ADMIN')