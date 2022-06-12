CREATE TABLE "category"(
    id SERIAL PRIMARY KEY,
    "name" varchar(100)
);

CREATE TABLE "refresh"(
    id SERIAL PRIMARY KEY,
    "modtime" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "source"(
    id varchar(50),
    "name" varchar(100),
    photo text,
    link text
);

CREATE TABLE "source_category"(
    source_Id varchar(50),
    category_Id int
);

