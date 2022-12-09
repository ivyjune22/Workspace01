create table member (
    num number not null,
    name varchar2(20) not null,
    rank number not null,
    birthday date not null
);

create sequence seq_mem nocycle nocache;

drop sequence seq_mem;

drop table member purge;

insert into member values (seq_mem.nextval, '수간호사1', 1, '1986-01-11');
insert into member values (seq_mem.nextval, '간호사1', 2, '1996-12-15');

update member set name='간호조무사1', rank=3, birthday='1994-02-14' where num=1;

select * from member;

select * from (select rownum rn, tt.* from
(select * from member order by seq_mem desc) tt)
where rn>=1 and rn<=20;

delete member where num=2;

commit;