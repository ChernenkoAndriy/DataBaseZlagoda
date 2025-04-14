--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-04-13 21:54:22

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 858 (class 1247 OID 24710)
-- Name: Roles; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public."Roles" AS ENUM (
    'Cashier',
    'Manager'
);


ALTER TYPE public."Roles" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 24829)
-- Name: Authorization_Data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Authorization_Data" (
    login character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    id_employee uuid NOT NULL
);


ALTER TABLE public."Authorization_Data" OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24643)
-- Name: Category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Category" (
    category_number integer NOT NULL,
    category_name character varying(50) NOT NULL
);


ALTER TABLE public."Category" OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24642)
-- Name: Category_category_number_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Category_category_number_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Category_category_number_seq" OWNER TO postgres;

--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 217
-- Name: Category_category_number_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Category_category_number_seq" OWNED BY public."Category".category_number;


--
-- TOC entry 221 (class 1259 OID 24755)
-- Name: Check; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Check" (
    check_number uuid DEFAULT gen_random_uuid() NOT NULL,
    card_number uuid,
    print_date timestamp without time zone NOT NULL,
    sum_total numeric(13,4) NOT NULL,
    vat numeric(13,4),
    id_employee uuid
);


ALTER TABLE public."Check" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24726)
-- Name: Customer_Card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Customer_Card" (
    card_number uuid DEFAULT gen_random_uuid() NOT NULL,
    cust_surname character varying(50) NOT NULL,
    cust_name character(50) NOT NULL,
    cust_patronymic character varying(50),
    phone_number character varying(13) NOT NULL,
    city character varying(50),
    street character varying(50),
    zip_code character varying(9),
    percent integer NOT NULL
);


ALTER TABLE public."Customer_Card" OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24720)
-- Name: Employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Employee" (
    id_employee uuid DEFAULT gen_random_uuid() NOT NULL,
    empl_surname character varying(50) NOT NULL,
    empl_name character varying(50) NOT NULL,
    empl_role public."Roles" NOT NULL,
    empl_patronymic character varying(50),
    salary numeric(13,4) NOT NULL,
    date_of_birth date NOT NULL,
    date_of_start date NOT NULL,
    phone_number character varying(13) NOT NULL,
    city character varying(50) NOT NULL,
    street character varying(50) NOT NULL,
    zip_code character varying(9) NOT NULL
);


ALTER TABLE public."Employee" OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24781)
-- Name: Product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Product" (
    id_product integer NOT NULL,
    category_number integer NOT NULL,
    product_name character varying(50) NOT NULL,
    characteristics character varying(100) NOT NULL
);


ALTER TABLE public."Product" OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24780)
-- Name: Product_id_product_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Product_id_product_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."Product_id_product_seq" OWNER TO postgres;

--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 222
-- Name: Product_id_product_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Product_id_product_seq" OWNED BY public."Product".id_product;


--
-- TOC entry 225 (class 1259 OID 24812)
-- Name: Sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Sale" (
    product_number integer NOT NULL,
    selling_price numeric(13,4) NOT NULL,
    "UPC" uuid NOT NULL,
    check_number uuid NOT NULL
);


ALTER TABLE public."Sale" OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24795)
-- Name: Store_Product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Store_Product" (
    "UPC" uuid DEFAULT gen_random_uuid() NOT NULL,
    "UPC_prom" uuid,
    id_product integer NOT NULL,
    selling_price numeric(13,4) NOT NULL,
    products_number integer NOT NULL,
    promotional_product boolean NOT NULL
);


ALTER TABLE public."Store_Product" OWNER TO postgres;

--
-- TOC entry 4727 (class 2604 OID 24646)
-- Name: Category category_number; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category" ALTER COLUMN category_number SET DEFAULT nextval('public."Category_category_number_seq"'::regclass);


--
-- TOC entry 4731 (class 2604 OID 24784)
-- Name: Product id_product; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product" ALTER COLUMN id_product SET DEFAULT nextval('public."Product_id_product_seq"'::regclass);


--
-- TOC entry 4919 (class 0 OID 24829)
-- Dependencies: 226
-- Data for Name: Authorization_Data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Authorization_Data" (login, password, id_employee) FROM stdin;
\.


--
-- TOC entry 4911 (class 0 OID 24643)
-- Dependencies: 218
-- Data for Name: Category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Category" (category_number, category_name) FROM stdin;
1	Meat Products
2	Sea Products
3	Dairy Products
4	Fruits and Vegetables
5	Bakery
6	Drinks
7	Alcoholic Drinks
\.


--
-- TOC entry 4914 (class 0 OID 24755)
-- Dependencies: 221
-- Data for Name: Check; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Check" (check_number, card_number, print_date, sum_total, vat, id_employee) FROM stdin;
a4577238-b696-4546-a97e-2bfa2d75ddde	4d679869-fefd-4998-9cd2-243327ac9803	2025-04-06 17:04:39.086743	820.7640	136.7940	da6bad0b-f79e-451a-b7ad-fc8a39a5278f
b92b79ea-1781-49d2-acf5-faa4b9e990e5	ac493364-e6c2-4d74-bd1b-c6210f407bc3	2025-04-06 17:04:39.086743	989.3280	164.8880	5f560bff-7f00-439e-a6c5-ed8a5e314234
f548f038-7a1e-4a97-8708-37d0e7e3fb18	8bdd2d96-a917-4a04-8659-79c27f799b05	2025-04-06 17:04:39.086743	374.1240	62.3540	e8645922-20d5-4430-8bb0-d72069acbb51
2bb041b3-8b18-47ec-8296-09c31fc74e1f	351992ea-36b1-42f6-bb9e-6e60efaee3ef	2025-04-06 17:04:39.086743	687.2280	114.5380	dd659214-399f-48b3-a888-c512fcfbe53e
f5d7ce6f-5123-4145-bc2c-d766f732ecfe	6cdde7c1-70d5-46aa-becf-55a2518e4eac	2025-04-06 17:04:39.086743	1057.7400	176.2900	2db9bf7e-5dc5-45b0-8ebf-3c00333ae44c
567010b4-c343-47d9-ac91-991ff2c4533d	3c9eb775-84a8-414d-b4e8-041b598835fc	2025-04-06 17:04:39.086743	258.5160	43.0860	127b9147-dbbf-4650-ba47-d19d8406ff05
15c65cae-61eb-4866-97ff-17cb8caff8ae	d858cbd1-676f-4c96-acef-9a1279286fde	2025-04-06 17:04:39.086743	989.3280	164.8880	567fddbb-0c38-42b4-97b1-7ce1946b496d
b211227f-e0f7-4d6f-a357-7fe74edc06bb	f0792e53-4c4c-4416-bf95-c59c02e661db	2025-04-06 17:04:39.086743	1057.7400	176.2900	a59d26d4-ff36-45e6-8af7-7f320ea6c8a2
3f85441a-ce12-4993-8c8e-cc2454e725a5	bb6700a2-f12e-470a-896e-f0b6eec2bdbe	2025-04-06 17:04:39.086743	687.2280	114.5380	ceba54f0-69d9-4cb4-8c64-8f600bc8a6dc
54087824-a60a-4e5c-a7b1-44723f0e4428	38872034-90fc-4e18-8b0b-d4d7ab8f1d23	2025-04-06 17:04:39.086743	1057.7400	176.2900	2ae3d575-d9b3-4e27-8214-8118cf8cfc8b
8c374203-98ad-4946-8d7f-a3566b51ef9c	a6ac44f8-2e6d-483c-af69-8bfed68ac9d5	2025-04-06 17:04:39.086743	820.7640	136.7940	da6bad0b-f79e-451a-b7ad-fc8a39a5278f
768dad32-5830-42cb-bbec-f2c873dcd62a	5a4a6cef-c5fd-4e2f-a76c-b3e6b764676c	2025-04-06 17:04:39.086743	820.7640	136.7940	5f560bff-7f00-439e-a6c5-ed8a5e314234
9246a7e2-d9f5-4b25-ab51-8e2f9aa19a97	84cae16a-5507-409c-b2c2-3c2f3885072b	2025-04-06 17:04:39.086743	989.3280	164.8880	e8645922-20d5-4430-8bb0-d72069acbb51
a4da821e-6a6d-44a7-95ad-a2b737437024	528650c0-c7ea-4ae7-b49c-0fa106fc8c6b	2025-04-06 17:04:39.086743	687.2280	114.5380	dd659214-399f-48b3-a888-c512fcfbe53e
fdd19b0d-fcc2-4a89-99a6-4d08675a0792	2291f25d-423d-411f-b06c-ff765087c5af	2025-04-06 17:04:39.086743	312.5280	52.0880	2db9bf7e-5dc5-45b0-8ebf-3c00333ae44c
a406693a-9722-4501-8385-15e427156ea4	d8a7a6df-49a5-4868-a9a4-5fc574ffaad0	2025-04-06 17:04:39.086743	374.1240	62.3540	127b9147-dbbf-4650-ba47-d19d8406ff05
921232e8-5d70-42b7-a91e-0197fda766b3	43c48c34-832b-49ca-bcb0-f497da4809a2	2025-04-06 17:04:39.086743	258.5160	43.0860	567fddbb-0c38-42b4-97b1-7ce1946b496d
6be767c8-b392-4e5e-9d55-da570fafa652	f4f66901-e87b-47f6-95e3-6fbb910a6093	2025-04-06 17:04:39.086743	374.1240	62.3540	a59d26d4-ff36-45e6-8af7-7f320ea6c8a2
05eb046c-c486-4d32-952c-21c7e50c20fd	4c32a3af-113d-450d-baf9-565c99660919	2025-04-06 17:04:39.086743	258.5160	43.0860	ceba54f0-69d9-4cb4-8c64-8f600bc8a6dc
312d01eb-4f1f-4e91-bf76-cca295d82ece	2091f228-67bb-42a4-a4cb-02308293cd26	2025-04-06 17:04:39.086743	820.7640	136.7940	2ae3d575-d9b3-4e27-8214-8118cf8cfc8b
\.


--
-- TOC entry 4913 (class 0 OID 24726)
-- Dependencies: 220
-- Data for Name: Customer_Card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Customer_Card" (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) FROM stdin;
4d679869-fefd-4998-9cd2-243327ac9803	Shevchenko	Andrii                                            	Petrovych	+380931000001	Kyiv	Khreshchatyk 15	01001	5
ac493364-e6c2-4d74-bd1b-c6210f407bc3	Bondarenko	Olena                                             	Ivanivna	+380931000002	Lviv	Shevchenka 22	79000	3
8bdd2d96-a917-4a04-8659-79c27f799b05	Melnyk	Vitalii                                           	Serhiyovych	+380931000003	Odesa	Deribasivska 4	65000	10
351992ea-36b1-42f6-bb9e-6e60efaee3ef	Tkachenko	Iryna                                             	Volodymyrivna	+380931000004	Kharkiv	Sumska 17	61000	7
6cdde7c1-70d5-46aa-becf-55a2518e4eac	Kovalenko	Oleksii                                           	Yuriyovych	+380931000005	Dnipro	Polya 1	49000	4
3c9eb775-84a8-414d-b4e8-041b598835fc	Kravchenko	Yuliia                                            	Mykolayivna	+380931000006	Vinnytsia	Soborna 6	21000	2
d858cbd1-676f-4c96-acef-9a1279286fde	Lysenko	Bohdan                                            	Stepanovych	+380931000007	Rivne	Soborna 20	33000	6
f0792e53-4c4c-4416-bf95-c59c02e661db	Mazur	Natalia                                           	Dmytrivna	+380931000008	Ternopil	Zluky 7	46000	1
bb6700a2-f12e-470a-896e-f0b6eec2bdbe	Didenko	Andriana                                          	Valentynivna	+380931000009	Ivano-Frankivsk	Nezalezhnosti 2	76000	8
38872034-90fc-4e18-8b0b-d4d7ab8f1d23	Savchenko	Denys                                             	Olehovych	+380931000010	Zhytomyr	Peremohy 10	10000	9
a6ac44f8-2e6d-483c-af69-8bfed68ac9d5	Zahorodnia	Inna                                              	Borysivna	+380931000011	Lutsk	Voli 5	43000	3
5a4a6cef-c5fd-4e2f-a76c-b3e6b764676c	Horobets	Taras                                             	Petrovych	+380931000012	Poltava	Europeiska 9	36000	5
84cae16a-5507-409c-b2c2-3c2f3885072b	Yurchenko	Olha                                              	Anatoliivna	+380931000013	Uzhhorod	Kapushanska 12	88000	7
528650c0-c7ea-4ae7-b49c-0fa106fc8c6b	Pavlenko	Artem                                             	Olehivych	+380931000014	Khmelnytskyi	Proskurivska 18	29000	0
2291f25d-423d-411f-b06c-ff765087c5af	Hrytsenko	Mariia                                            	Leonidivna	+380931000015	Cherkasy	Shevchenka 25	18000	6
d8a7a6df-49a5-4868-a9a4-5fc574ffaad0	Boiko	Roman                                             	Yevhenovych	+380931000016	Mykolaiv	Soborna 33	54000	2
43c48c34-832b-49ca-bcb0-f497da4809a2	Petrenko	Sofiia                                            	Romanivna	+380931000017	Sumy	Kharkivska 12	40000	10
f4f66901-e87b-47f6-95e3-6fbb910a6093	Rudenko	Daryna                                            	Ihorivna	+380931000018	Kropyvnytskyi	Perspektyvna 5	25000	4
4c32a3af-113d-450d-baf9-565c99660919	Kostenko	Yurii                                             	Serhiyovych	+380931000019	Chernivtsi	Holovna 8	58000	5
2091f228-67bb-42a4-a4cb-02308293cd26	Vasylenko	Kateryna                                          	Dmytrivna	+380931000020	Chernihiv	Myru 21	14000	3
\.


--
-- TOC entry 4912 (class 0 OID 24720)
-- Dependencies: 219
-- Data for Name: Employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Employee" (id_employee, empl_surname, empl_name, empl_role, empl_patronymic, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) FROM stdin;
ceba54f0-69d9-4cb4-8c64-8f600bc8a6dc	Honchar	Vitalii	Cashier	Serhiyovych	15250.0000	1989-09-14	2020-12-12	+380671234575	Rivne	Soborna 14	330000000
567fddbb-0c38-42b4-97b1-7ce1946b496d	Bondar	Tetiana	Cashier	Mykhailivna	15300.0000	1991-06-18	2020-09-10	+380731234573	Zhytomyr	Peremohy 12	100000000
924561ca-a887-4bd8-a8ef-eb5c9995c30b	Tarasenko	Hanna	Manager	Yevhenivna	0.2500	1985-07-23	2018-08-17	+380501234585	Mykolaiv	Soborna 33	540000000
2db9bf7e-5dc5-45b0-8ebf-3c00333ae44c	Ivanova	Maria	Cashier	Yuriyivna	14900.0000	1998-04-30	2023-02-01	+380681234571	Dnipro	Polya 17	490000000
2ae3d575-d9b3-4e27-8214-8118cf8cfc8b	Tkachenko	Kateryna	Cashier	Dmytrivna	15000.0000	1996-01-07	2023-03-10	+380991234576	Vinnytsia	Kotsyubynskoho 2	210000000
e0acf129-abda-4f03-b860-850d91b92a48	Marchuk	Victor	Manager	Stepanovych	100.0000	1983-10-05	2015-07-01	+380671234579	Lutsk	Vynnychenka 4	430000000
228d5ad3-5afc-4cc3-99f1-48e53e5c86d2	Zakharchenko	Svitlana	Manager	Bohdanivna	23500.0000	1979-06-19	2014-01-20	+380661234580	Uzhhorod	Kapushanska 9	880000000
a59d26d4-ff36-45e6-8af7-7f320ea6c8a2	Sydorenko	Yulia	Cashier	Olehivna	15400.0000	1993-02-22	2021-05-20	+380661234574	Berlin	Europeiska 9	360000000
127b9147-dbbf-4650-ba47-d19d8406ff05	Romaniuk	Oleks	Cashier	Andriyovych	15050.0000	1985-12-05	2018-11-01	+380991234572	Chernivtsi	Holovna 5	580000000
dd659214-399f-48b3-a888-c512fcfbe53e	Petrenko	Andrii	Cashier	Ihorovych	15100.0000	1992-11-23	2019-08-20	+380501234570	Kharkiv	Sumska 35	610000000
5f560bff-7f00-439e-a6c5-ed8a5e314234	Koval	Ihor	Cashier	Petrovych	15200.0000	1987-07-08	2021-01-10	+380671234568	Lviv	Shevchenka 20	790000000
da6bad0b-f79e-451a-b7ad-fc8a39a5278f	Smith	Olena	Cashier	Mykolaiovych	15000.0000	1990-05-12	2020-03-01	+380631234567	Kyiv	Khreshchatyk 10	010010000
e8645922-20d5-4430-8bb0-d72069acbb51	Melnyk	Oksana	Cashier	Vasylivna	14800.0000	1995-03-15	2022-06-15	+380931234569	Odesa	Deribasivska 1	650000000
3db1d182-46a1-43c4-9677-cb8bbebbcd2b	Ponomarenko	Maksym	Manager	Viktorovych	23200.0000	1987-11-13	2020-11-11	+380681234586	Sumy	Kharkivska 12	400000000
d01c9347-b040-467f-9ef3-916ea9deb3af	Bilyi	Serhii	Manager	Mykolaiovych	24800.0000	1992-10-18	2022-04-05	+380661234584	Kropyvnytskyi	Velyka Perspektyvna 5	250000000
e45a81f8-82a1-46c8-b3b9-efe27a2e85b7	Chorna	Iryna	Manager	Valentynivna	24700.0000	1986-09-29	2019-09-09	+380731234583	Khmelnytskyi	Proskurivska 22	290000000
ccee9d89-8955-4714-9c9f-20a244b6ffc6	Lytvynenko	Inna	Manager	Ivanivna	24000.0000	1990-03-11	2018-02-14	+380931234578	Kharkiv	Naukova 11	611000000
\.


--
-- TOC entry 4916 (class 0 OID 24781)
-- Dependencies: 223
-- Data for Name: Product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Product" (id_product, category_number, product_name, characteristics) FROM stdin;
1	1	Beef Steak	Grass-fed, 500g
2	1	Chicken Breast	Boneless, 1kg
3	1	Pork Ribs	Marinated, 800g
4	1	Lamb Chops	Fresh, 600g
5	2	Salmon Fillet	Norwegian, 400g
6	2	Shrimps	Cleaned, 1kg
7	2	Tuna Steak	Frozen, 300g
8	2	Mussels	In Shell, 500g
9	3	Milk	Whole, 1L
10	3	Cheddar Cheese	Aged, 200g
11	3	Yogurt	Strawberry, 125g
12	3	Butter	Unsalted, 250g
13	4	Apples	Red, 1kg
14	4	Bananas	Organic, 1kg
15	4	Carrots	Fresh, 500g
16	4	Spinach	Washed, 300g
17	5	White Bread	Sliced, 500g
18	5	Croissant	Butter, 3pcs
19	5	Wholegrain Bread	With seeds, 400g
20	5	Baguette	Crispy, 1pc
21	6	Orange Juice	No sugar, 1L
22	6	Mineral Water	Sparkling, 1.5L
23	6	Cola	Classic, 1L
24	6	Green Tea	Cold Brew, 500ml
25	7	Red Wine	Dry, 750ml
26	7	Beer	Lager, 500ml
27	7	Whiskey	Aged 12 years, 700ml
28	7	Vodka	Classic, 500ml
\.


--
-- TOC entry 4918 (class 0 OID 24812)
-- Dependencies: 225
-- Data for Name: Sale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Sale" (product_number, selling_price, "UPC", check_number) FROM stdin;
3	201.4800	6bca7d90-6938-4e9a-aec0-f5249df3c6dd	a4577238-b696-4546-a97e-2bfa2d75ddde
1	79.5300	842149e3-be2a-48ed-9a9c-f21810e6b2f0	a4577238-b696-4546-a97e-2bfa2d75ddde
2	130.2200	b9608ecc-c5c4-4ef1-879b-553da084257f	b92b79ea-1781-49d2-acf5-faa4b9e990e5
4	141.0000	a6f34dfe-8ce4-4372-919e-7c35e41f2413	b92b79ea-1781-49d2-acf5-faa4b9e990e5
1	23.0900	5dad3d00-0c8b-4e9b-b681-23fd23438918	f548f038-7a1e-4a97-8708-37d0e7e3fb18
2	144.3400	1c3eee00-66e3-48ed-a742-09ca4cff2811	f548f038-7a1e-4a97-8708-37d0e7e3fb18
3	157.9200	87489a80-cc53-4d65-a9d6-4938fb4bf968	2bb041b3-8b18-47ec-8296-09c31fc74e1f
1	98.9300	635a13af-340a-4b99-894d-c7f2285cc725	2bb041b3-8b18-47ec-8296-09c31fc74e1f
2	195.5200	af5b2690-3f2c-43fc-949b-41f0a2258038	f5d7ce6f-5123-4145-bc2c-d766f732ecfe
3	163.4700	a33895c7-3abf-448c-986b-781bfd8550ac	f5d7ce6f-5123-4145-bc2c-d766f732ecfe
1	17.1900	b31bf935-faf3-4be5-bdbd-dce5998cba34	567010b4-c343-47d9-ac91-991ff2c4533d
2	99.1200	a8dfc473-5588-40a8-a520-c286be6d5bba	567010b4-c343-47d9-ac91-991ff2c4533d
3	201.4800	6bca7d90-6938-4e9a-aec0-f5249df3c6dd	768dad32-5830-42cb-bbec-f2c873dcd62a
1	79.5300	842149e3-be2a-48ed-9a9c-f21810e6b2f0	768dad32-5830-42cb-bbec-f2c873dcd62a
2	130.2200	b9608ecc-c5c4-4ef1-879b-553da084257f	15c65cae-61eb-4866-97ff-17cb8caff8ae
4	141.0000	a6f34dfe-8ce4-4372-919e-7c35e41f2413	15c65cae-61eb-4866-97ff-17cb8caff8ae
1	23.0900	5dad3d00-0c8b-4e9b-b681-23fd23438918	a406693a-9722-4501-8385-15e427156ea4
2	144.3400	1c3eee00-66e3-48ed-a742-09ca4cff2811	a406693a-9722-4501-8385-15e427156ea4
3	157.9200	87489a80-cc53-4d65-a9d6-4938fb4bf968	3f85441a-ce12-4993-8c8e-cc2454e725a5
1	98.9300	635a13af-340a-4b99-894d-c7f2285cc725	3f85441a-ce12-4993-8c8e-cc2454e725a5
2	195.5200	af5b2690-3f2c-43fc-949b-41f0a2258038	b211227f-e0f7-4d6f-a357-7fe74edc06bb
3	163.4700	a33895c7-3abf-448c-986b-781bfd8550ac	b211227f-e0f7-4d6f-a357-7fe74edc06bb
1	17.1900	b31bf935-faf3-4be5-bdbd-dce5998cba34	921232e8-5d70-42b7-a91e-0197fda766b3
2	99.1200	a8dfc473-5588-40a8-a520-c286be6d5bba	921232e8-5d70-42b7-a91e-0197fda766b3
3	201.4800	6bca7d90-6938-4e9a-aec0-f5249df3c6dd	312d01eb-4f1f-4e91-bf76-cca295d82ece
1	79.5300	842149e3-be2a-48ed-9a9c-f21810e6b2f0	312d01eb-4f1f-4e91-bf76-cca295d82ece
2	130.2200	b9608ecc-c5c4-4ef1-879b-553da084257f	9246a7e2-d9f5-4b25-ab51-8e2f9aa19a97
4	141.0000	a6f34dfe-8ce4-4372-919e-7c35e41f2413	9246a7e2-d9f5-4b25-ab51-8e2f9aa19a97
1	23.0900	5dad3d00-0c8b-4e9b-b681-23fd23438918	6be767c8-b392-4e5e-9d55-da570fafa652
2	144.3400	1c3eee00-66e3-48ed-a742-09ca4cff2811	6be767c8-b392-4e5e-9d55-da570fafa652
3	157.9200	87489a80-cc53-4d65-a9d6-4938fb4bf968	a4da821e-6a6d-44a7-95ad-a2b737437024
1	98.9300	635a13af-340a-4b99-894d-c7f2285cc725	a4da821e-6a6d-44a7-95ad-a2b737437024
2	195.5200	af5b2690-3f2c-43fc-949b-41f0a2258038	54087824-a60a-4e5c-a7b1-44723f0e4428
3	163.4700	a33895c7-3abf-448c-986b-781bfd8550ac	54087824-a60a-4e5c-a7b1-44723f0e4428
1	17.1900	b31bf935-faf3-4be5-bdbd-dce5998cba34	05eb046c-c486-4d32-952c-21c7e50c20fd
2	99.1200	a8dfc473-5588-40a8-a520-c286be6d5bba	05eb046c-c486-4d32-952c-21c7e50c20fd
3	201.4800	6bca7d90-6938-4e9a-aec0-f5249df3c6dd	8c374203-98ad-4946-8d7f-a3566b51ef9c
1	79.5300	842149e3-be2a-48ed-9a9c-f21810e6b2f0	8c374203-98ad-4946-8d7f-a3566b51ef9c
2	130.2200	b9608ecc-c5c4-4ef1-879b-553da084257f	fdd19b0d-fcc2-4a89-99a6-4d08675a0792
\.


--
-- TOC entry 4917 (class 0 OID 24795)
-- Dependencies: 224
-- Data for Name: Store_Product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Store_Product" ("UPC", "UPC_prom", id_product, selling_price, products_number, promotional_product) FROM stdin;
6bca7d90-6938-4e9a-aec0-f5249df3c6dd	\N	1	67.1600	87	f
842149e3-be2a-48ed-9a9c-f21810e6b2f0	\N	2	79.5300	72	f
b9608ecc-c5c4-4ef1-879b-553da084257f	\N	3	65.1100	21	f
a6f34dfe-8ce4-4372-919e-7c35e41f2413	\N	4	35.2500	71	f
5dad3d00-0c8b-4e9b-b681-23fd23438918	\N	5	23.0900	37	f
1c3eee00-66e3-48ed-a742-09ca4cff2811	\N	6	72.1700	49	f
87489a80-cc53-4d65-a9d6-4938fb4bf968	\N	7	52.6400	70	f
635a13af-340a-4b99-894d-c7f2285cc725	\N	8	98.9300	18	f
af5b2690-3f2c-43fc-949b-41f0a2258038	\N	9	97.7600	32	f
a33895c7-3abf-448c-986b-781bfd8550ac	\N	10	54.4900	59	f
b31bf935-faf3-4be5-bdbd-dce5998cba34	\N	11	17.1900	22	f
a8dfc473-5588-40a8-a520-c286be6d5bba	\N	12	49.5600	58	f
1dd6b3a7-e19e-4c47-bd39-8516abd1c39e	\N	13	51.3300	10	f
ed40c255-1d5c-4618-a55d-b1a3cb09d55a	\N	14	26.2300	59	f
051d04a9-814f-4ef4-a1a8-63346103417a	\N	15	68.7300	37	f
ea356f9b-b455-45c7-9a34-60008a1c0482	\N	16	32.3500	13	f
d8a41692-3544-4bd9-b4b9-0e90808d8e36	\N	17	47.7000	87	f
312c5e36-b28a-494c-87b7-8f4620fc34cf	\N	18	45.6600	48	f
8a6c588f-0666-4435-9b32-fc8c7dda22aa	\N	19	31.6800	40	f
3c4e064c-ba78-4425-bc6f-385f3e79495c	\N	20	22.0500	34	f
2e3a40ff-275e-46d6-a976-0c3b7ed4caf0	\N	21	31.3500	48	f
c292c6ba-3b73-45a9-99bb-79c47fd63d67	\N	22	71.7200	50	f
ea2b2fa1-4c2f-45ed-9292-75bad2d13da1	\N	23	73.6300	66	f
870d34ab-dfa7-470a-8cff-3a529b0abdb5	\N	24	75.8000	68	f
2c9064cc-e85e-443b-bfad-6de17dff05a2	\N	25	73.6900	15	f
28cc41d6-f066-4f5a-83dc-b48dcc5e6a4a	\N	26	48.5700	22	f
e1cbc38b-d470-4b0b-a3a0-1890e5b33734	\N	27	86.1200	27	f
4ed27033-7237-49a1-b471-6fc9f52f0ab3	\N	28	80.4000	14	f
acb30497-809f-46c1-9485-3061ef638efe	842149e3-be2a-48ed-9a9c-f21810e6b2f0	2	63.6200	72	t
728465ff-cd2d-4480-902f-ab955734c082	a6f34dfe-8ce4-4372-919e-7c35e41f2413	4	28.2000	71	t
07bbfb54-bd48-4abc-a4f9-3f8c69f10302	1c3eee00-66e3-48ed-a742-09ca4cff2811	6	57.7400	49	t
34d65767-901c-4a76-8315-8104d9f90b05	635a13af-340a-4b99-894d-c7f2285cc725	8	79.1400	18	t
8e3293f5-9b93-4c58-88f1-e1c0c869d010	a33895c7-3abf-448c-986b-781bfd8550ac	10	43.5900	59	t
c97e525b-607e-4574-b151-e32f3c7099d3	a8dfc473-5588-40a8-a520-c286be6d5bba	12	39.6500	58	t
d0c3e984-6544-4701-af87-cf673c3d34af	ed40c255-1d5c-4618-a55d-b1a3cb09d55a	14	20.9800	59	t
15dbfe01-2ea3-47ad-8eca-fce5e5b709bb	ea356f9b-b455-45c7-9a34-60008a1c0482	16	25.8800	13	t
5abf8357-1195-4878-b1c8-654c12ce4989	312c5e36-b28a-494c-87b7-8f4620fc34cf	18	36.5300	48	t
d730e254-fe3d-47e6-8e5a-3dffebd163ad	3c4e064c-ba78-4425-bc6f-385f3e79495c	20	17.6400	34	t
c88f3024-d48f-4d07-a4c5-d0126ad36cb7	c292c6ba-3b73-45a9-99bb-79c47fd63d67	22	57.3800	50	t
bd8d70fe-d1a8-4318-96c8-832f18130f94	870d34ab-dfa7-470a-8cff-3a529b0abdb5	24	60.6400	68	t
2413c5ad-ab4d-4a66-bbd2-5e5b55a64bbb	28cc41d6-f066-4f5a-83dc-b48dcc5e6a4a	26	38.8600	22	t
ec61d353-d45b-435f-aa70-577f93da1b7c	4ed27033-7237-49a1-b471-6fc9f52f0ab3	28	64.3200	14	t
\.


--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 217
-- Name: Category_category_number_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Category_category_number_seq"', 7, true);


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 222
-- Name: Product_id_product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Product_id_product_seq"', 28, true);


--
-- TOC entry 4756 (class 2606 OID 24833)
-- Name: Authorization_Data Authorization_Data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Authorization_Data"
    ADD CONSTRAINT "Authorization_Data_pkey" PRIMARY KEY (login);


--
-- TOC entry 4734 (class 2606 OID 24648)
-- Name: Category Category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category"
    ADD CONSTRAINT "Category_pkey" PRIMARY KEY (category_number);


--
-- TOC entry 4744 (class 2606 OID 24760)
-- Name: Check Check_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT "Check_pkey" PRIMARY KEY (check_number);


--
-- TOC entry 4740 (class 2606 OID 24731)
-- Name: Customer_Card Customer_Card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Customer_Card"
    ADD CONSTRAINT "Customer_Card_pkey" PRIMARY KEY (card_number);


--
-- TOC entry 4738 (class 2606 OID 24725)
-- Name: Employee Employee_pkey1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_pkey1" PRIMARY KEY (id_employee);


--
-- TOC entry 4746 (class 2606 OID 24786)
-- Name: Product Product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (id_product);


--
-- TOC entry 4754 (class 2606 OID 24816)
-- Name: Sale Sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT "Sale_pkey" PRIMARY KEY ("UPC", check_number);


--
-- TOC entry 4751 (class 2606 OID 24800)
-- Name: Store_Product Store_Product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT "Store_Product_pkey" PRIMARY KEY ("UPC");


--
-- TOC entry 4736 (class 2606 OID 24679)
-- Name: Category category_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category"
    ADD CONSTRAINT category_name UNIQUE (category_name) INCLUDE (category_name);


--
-- TOC entry 4742 (class 2606 OID 24828)
-- Name: Customer_Card phone_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Customer_Card"
    ADD CONSTRAINT phone_number UNIQUE (phone_number);


--
-- TOC entry 4749 (class 2606 OID 24788)
-- Name: Product product_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT product_name UNIQUE (product_name) INCLUDE (product_name);


--
-- TOC entry 4747 (class 1259 OID 24794)
-- Name: fki_category_number; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_category_number ON public."Product" USING btree (category_number);


--
-- TOC entry 4752 (class 1259 OID 24811)
-- Name: fki_id_product; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_id_product ON public."Store_Product" USING btree (id_product);


--
-- TOC entry 4762 (class 2606 OID 24817)
-- Name: Sale UPC; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT "UPC" FOREIGN KEY ("UPC") REFERENCES public."Store_Product"("UPC") ON UPDATE CASCADE;


--
-- TOC entry 4760 (class 2606 OID 24801)
-- Name: Store_Product UPC_prom; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT "UPC_prom" FOREIGN KEY ("UPC_prom") REFERENCES public."Store_Product"("UPC") ON UPDATE CASCADE;


--
-- TOC entry 4757 (class 2606 OID 24761)
-- Name: Check card_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT card_number FOREIGN KEY (card_number) REFERENCES public."Customer_Card"(card_number) ON UPDATE CASCADE;


--
-- TOC entry 4759 (class 2606 OID 24789)
-- Name: Product category_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT category_number FOREIGN KEY (category_number) REFERENCES public."Category"(category_number) ON UPDATE CASCADE;


--
-- TOC entry 4763 (class 2606 OID 24822)
-- Name: Sale check_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT check_number FOREIGN KEY (check_number) REFERENCES public."Check"(check_number) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4764 (class 2606 OID 24834)
-- Name: Authorization_Data empl_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Authorization_Data"
    ADD CONSTRAINT empl_id FOREIGN KEY (id_employee) REFERENCES public."Employee"(id_employee);


--
-- TOC entry 4758 (class 2606 OID 24766)
-- Name: Check id_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT id_employee FOREIGN KEY (id_employee) REFERENCES public."Employee"(id_employee);


--
-- TOC entry 4761 (class 2606 OID 24806)
-- Name: Store_Product id_product; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT id_product FOREIGN KEY (id_product) REFERENCES public."Product"(id_product) ON UPDATE CASCADE ON DELETE SET NULL;


-- Completed on 2025-04-13 21:54:22

--
-- PostgreSQL database dump complete
--

