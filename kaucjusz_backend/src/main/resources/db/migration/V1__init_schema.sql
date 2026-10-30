CREATE EXTENSION IF NOT EXISTS postgis;


    create table machine_reviews (
        rating integer not null,
        created_at timestamp(6) with time zone not null,
        id uuid not null,
        machine_id uuid not null,
        user_id uuid not null,
        comment TEXT,
        primary key (id),
        constraint uq_machine_reviews_machine_user unique (machine_id, user_id)
    );

    create table pets (
        base_hunger integer not null,
        base_mood integer not null,
        level integer not null,
        xp integer not null,
        created_at timestamp(6) with time zone not null,
        last_interaction_at timestamp(6) with time zone not null,
        id uuid not null,
        user_id uuid not null unique,
        primary key (id)
    );

    create table point_transactions (
        amount integer not null,
        created_at timestamp(6) with time zone not null,
        id uuid not null,
        receipt_scan_id uuid,
        user_id uuid not null,
        transaction_type varchar(30) not null check ((transaction_type in ('SCAN','REFUND','FEED_PET','BUY_ITEM'))),
        primary key (id)
    );

    create table receipt_scans (
        bottles_count integer not null,
        deposit_amount numeric(6,2),
        points_awarded integer not null,
        printed_at timestamp(6) with time zone,
        scanned_at timestamp(6) with time zone not null,
        id uuid not null,
        matched_machine_id uuid,
        user_id uuid not null,
        status varchar(20) not null,
        type varchar(20) not null check ((type in ('OCR_SCAN','MANUAL_ENTRY'))),
        store_name varchar(100),
        receipt_fingerprint varchar(255) unique,
        scan_location geography(Point, 4326),
        primary key (id)
    );

    create table users (
        current_points integer not null,
        current_streak integer not null,
        last_scan_date date,
        lifetime_points integer not null,
        created_at timestamp(6) with time zone not null,
        deleted_at timestamp(6) with time zone,
        id uuid not null,
        nickname varchar(50) unique,
        region varchar(100),
        firebase_uid varchar(128) not null unique,
        primary key (id)
    );

    create table vending_machines (
        average_rating numeric(3,2) not null,
        reviews_count integer not null,
        created_at timestamp(6) with time zone not null,
        id uuid not null,
        reported_by_user_id uuid,
        status varchar(30) not null,
        name varchar(150) not null,
        photo_url varchar(500),
        address varchar(255) not null,
        location geography(Point, 4326) not null,
        opening_hours jsonb,
        primary key (id)
    );

    alter table if exists machine_reviews 
       add constraint FKi85vm3nk5m3cxl4c1p3xl6w1q 
       foreign key (machine_id) 
       references vending_machines;

    alter table if exists machine_reviews 
       add constraint FKcg70uhpfgj0nwcftuajlxm4ys 
       foreign key (user_id) 
       references users;

    alter table if exists pets 
       add constraint FKc47kjb41qf50bwgddm024m5xn 
       foreign key (user_id) 
       references users;

    alter table if exists point_transactions 
       add constraint FKp8ng4jq0v7y9nahwa5xsahe1t 
       foreign key (receipt_scan_id) 
       references receipt_scans;

    alter table if exists point_transactions 
       add constraint FKcqbvxpskb8p7mko50hokltu5b 
       foreign key (user_id) 
       references users;

    alter table if exists receipt_scans 
       add constraint FK8kvj5a9imtmy955tur1epctw7 
       foreign key (matched_machine_id) 
       references vending_machines;

    alter table if exists receipt_scans 
       add constraint FK1r8gcocyd04hbyg964df7mt82 
       foreign key (user_id) 
       references users;

    alter table if exists vending_machines 
       add constraint FK728tyd2exyjmx2s3dx8bqmed5 
       foreign key (reported_by_user_id) 
       references users;
