CREATE DATABASE springbatchdemo ENCODING 'UTF8';
\c springbatchdemo;
set timezone = 'Asia/Taipei';
show timezone;
set timezone = 'Asia/Taipei';
show timezone;

DROP TABLE IF EXISTS coffee;

CREATE TABLE coffee  (
                         coffee_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         brand VARCHAR(20),
                         origin VARCHAR(20),
                         characteristics VARCHAR(30)
);