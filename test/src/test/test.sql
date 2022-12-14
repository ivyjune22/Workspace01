create table member (
    num number not null,
    name varchar2(20) not null,
    rank number not null,
    birthday date not null
);

create sequence seq_mem nocycle nocache;

drop sequence seq_mem;

drop table member purge;

DECLARE
    cf varchar2(20) := '수';
    nu varchar2(20) := '간';
    su varchar2(20) := '조';
BEGIN
    FOR i IN 1..20
    LOOP
        insert into member values 
        (seq_mem.nextval, cf || i, 1,
        TO_DATE ( ROUND (DBMS_RANDOM.VALUE (1, 28)) 
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1, 12))
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1950, 2000)), 'DD-MM-YYYY' ));
    END LOOP;
    
    FOR i IN 1..40
    LOOP
        insert into member values 
        (seq_mem.nextval, nu || i, 2,
        TO_DATE ( ROUND (DBMS_RANDOM.VALUE (1, 28)) 
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1, 12))
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1950, 2000)), 'DD-MM-YYYY' ));
    END LOOP;
    
    FOR i IN 1..40
    LOOP
        insert into member values 
        (seq_mem.nextval, su || i, 3,
        TO_DATE ( ROUND (DBMS_RANDOM.VALUE (1, 28)) 
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1, 12))
                || '-'
                || ROUND (DBMS_RANDOM.VALUE (1950, 2000)), 'DD-MM-YYYY' ));
    END LOOP;
    COMMIT;
END;