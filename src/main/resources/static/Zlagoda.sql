--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-03-24 13:47:10

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 223 (class 1259 OID 24643)
-- Name: Category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Category" (
    category_number integer NOT NULL,
    category_name character varying(50) NOT NULL
);


ALTER TABLE public."Category" OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24642)
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
-- TOC entry 4910 (class 0 OID 0)
-- Dependencies: 222
-- Name: Category_category_number_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Category_category_number_seq" OWNED BY public."Category".category_number;


--
-- TOC entry 221 (class 1259 OID 24612)
-- Name: Check; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Check" (
    check_number character varying(10) NOT NULL,
    id_employee character varying(10) NOT NULL,
    card_number character varying(13),
    print_date timestamp without time zone NOT NULL,
    sum_total numeric(13,4) NOT NULL,
    vat numeric(13,4) NOT NULL
);


ALTER TABLE public."Check" OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24592)
-- Name: Customer_Card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Customer_Card" (
    card_number character varying(13) NOT NULL,
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
-- TOC entry 218 (class 1259 OID 24597)
-- Name: Employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Employee" (
    id_employee character varying(10) NOT NULL,
    empl_surname character varying(50) NOT NULL,
    empl_name character varying(50) NOT NULL,
    empl_patronymic character varying(50),
    empl_role character varying(10) NOT NULL,
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
-- TOC entry 225 (class 1259 OID 24650)
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
-- TOC entry 224 (class 1259 OID 24649)
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
-- TOC entry 4911 (class 0 OID 0)
-- Dependencies: 224
-- Name: Product_id_product_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Product_id_product_seq" OWNED BY public."Product".id_product;


--
-- TOC entry 219 (class 1259 OID 24602)
-- Name: Sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Sale" (
    product_number integer NOT NULL,
    selling_price numeric(13,4) NOT NULL,
    "UPC" character varying(12) NOT NULL,
    check_number character varying(10) NOT NULL
);


ALTER TABLE public."Sale" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24607)
-- Name: Store_Product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Store_Product" (
    "UPC" character varying(12) NOT NULL,
    "UPC_prom" character varying(12),
    id_product integer NOT NULL,
    selling_price numeric(13,4) NOT NULL,
    products_number integer NOT NULL,
    promotional_product boolean NOT NULL
);


ALTER TABLE public."Store_Product" OWNER TO postgres;

--
-- TOC entry 4720 (class 2604 OID 24646)
-- Name: Category category_number; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category" ALTER COLUMN category_number SET DEFAULT nextval('public."Category_category_number_seq"'::regclass);


--
-- TOC entry 4721 (class 2604 OID 24653)
-- Name: Product id_product; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product" ALTER COLUMN id_product SET DEFAULT nextval('public."Product_id_product_seq"'::regclass);


--
-- TOC entry 4902 (class 0 OID 24643)
-- Dependencies: 223
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
-- TOC entry 4900 (class 0 OID 24612)
-- Dependencies: 221
-- Data for Name: Check; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Check" (check_number, id_employee, card_number, print_date, sum_total, vat) FROM stdin;
CHK0001	E0003	000013	2025-02-10 14:23:45	120.5000	24.1000
CHK0002	E0005	\N	2025-02-12 17:45:12	75.9000	15.1800
CHK0003	E0010	000002	2025-02-14 11:30:05	200.3000	40.0600
CHK0004	E0010	\N	2025-02-18 09:05:22	55.0000	11.0000
CHK0005	E0005	000005	2025-02-19 20:14:33	180.7500	36.1500
CHK0006	E0006	\N	2025-02-21 16:55:44	95.6000	19.1200
CHK0007	E0011	000007	2025-02-25 10:23:11	220.0000	44.0000
CHK0008	E0006	\N	2025-02-28 19:40:50	60.3000	12.0600
CHK0009	E0005	000009	2025-03-01 13:55:28	145.4500	29.0900
CHK0010	E0005	\N	2025-03-02 08:30:15	80.0000	16.0000
CHK0011	E0008	000009	2025-03-05 14:10:37	170.2000	34.0400
CHK0012	E0010	\N	2025-03-07 18:25:43	55.7000	11.1400
CHK0013	E0002	000010	2025-03-10 12:50:55	210.0000	42.0000
CHK0014	E0002	\N	2025-03-12 15:10:12	99.9900	19.9980
CHK0015	E0013	000005	2025-03-14 09:40:18	125.8000	25.1600
CHK0016	E0010	\N	2025-03-16 11:33:45	65.5000	13.1000
CHK0017	E0008	000011	2025-03-18 17:20:37	185.7500	37.1500
CHK0018	E0010	\N	2025-03-20 20:05:23	73.4000	14.6800
CHK0019	E0005	000001	2025-03-22 13:10:56	140.0000	28.0000
CHK0020	E0013	\N	2025-03-25 08:50:22	50.2000	10.0400
\.


--
-- TOC entry 4896 (class 0 OID 24592)
-- Dependencies: 217
-- Data for Name: Customer_Card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Customer_Card" (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) FROM stdin;
000001	Shevchenko	Ivan                                              	Petrovych	380501234567	Kyiv	Khreshchatyk St, 1	01001	5
000002	Bondarenko	Oksana                                            	Ivanivna	380671234568	Lviv	Shevchenka St, 5	79000	10
000003	Melnyk	Andriy                                            	Volodymyrovych	380931234569	Odesa	Deribasivska St, 12	65000	7
000004	Tkachenko	Olena                                             	Serhiivna	380501234570	Dnipro	Yavornytskogo Ave, 20	49000	3
000005	Kovalenko	Dmytro                                            	Mykolaiovych	380671234571	Kharkiv	Sumska St, 8	61000	8
000006	Hrytsenko	Nadiya                                            	Stepanivna	380931234572	Zhytomyr	Peremohy St, 15	10000	4
000007	Pavlenko	Mykhailo                                          	Oleksandrovych	380501234573	Chernihiv	Myru Ave, 7	14000	6
000008	Rudenko	Yuliia                                            	Anatoliivna	380671234574	Vinnytsia	Soborna St, 10	21000	9
000009	Moroz	Serhiy                                            	Olegovych	380931234575	Poltava	Nebesnoi Sotni St, 3	36000	2
000010	Lysenko	Kateryna                                          	Borysivna	380501234576	Ternopil	Halytska St, 14	46000	5
000011	Savchenko	Oleh                                              	Vasylovych	380671234577	Ivano-Frankivsk	Nezalezhnosti St, 6	76000	7
000012	Didenko	Anastasiia                                        	Petrova	380931234578	Rivne	Soborna St, 11	33000	6
000013	Zaitsev	Vladyslav                                         	Ihorovych	380501234579	Uzhhorod	Korzo St, 4	88000	10
000014	Kostenko	Bruno                                             	Adolfovych	380671234580	Lutsk	Volodymyrska St, 9	43000	3
000015	Shapoval	Adam                                              	Georgevych	380931234581	Chernivtsi	Holovna St, 5	58000	4
\.


--
-- TOC entry 4897 (class 0 OID 24597)
-- Dependencies: 218
-- Data for Name: Employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Employee" (id_employee, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) FROM stdin;
E0001	Shevchenko	Ivan	Petrovych	Manager	25000.0000	1985-03-12	2015-06-01	380501234567	Kyiv	Khreshchatyk St, 1	01001
E0002	Bondarenko	Oksana	Ivanivna	Cashier	15000.0000	1992-07-22	2019-08-15	380671234568	Lviv	Shevchenka St, 5	79000
E0004	Tkachenko	Olena	Serhiivna	Manager	27000.0000	1980-05-05	2010-09-01	380501234570	Dnipro	Yavornytskogo Ave, 20	49000
E0006	Hrytsenko	Nadiya	\N	Cashier	14500.0000	1993-06-25	2021-05-10	380931234572	Zhytomyr	Peremohy St, 15	10000
E0009	Moroz	Serhiy	Olegovych	Manager	26000.0000	1982-02-28	2008-03-25	380931234575	Poltava	Nebesnoi Sotni St, 3	36000
E0011	Savchenko	Oleh	Vasylovych	Cashier	15000.0000	1996-07-09	2023-06-18	380671234577	Ivano-Frankivsk	Nezalezhnosti St, 6	76000
E0015	Shapoval	Artem	Leonidovych	Manager	28000.0000	1979-11-01	2007-02-18	380931234581	Chernivtsi	Holovna St, 5	58000
E0003	Melnyk	Andriy	\N	Cashier	18000.0000	1990-11-30	2018-02-10	380931234569	Odesa	Deribasivska St, 12	65000
E0005	Kovalenko	Dmytro	Mykolaiovych	Cashier	16000.0000	1995-01-17	2020-11-20	380671234571	Kharkiv	Sumska St, 8	61000
E0007	Pavlenko	Mykhailo	Oleksandrovych	Manager	17500.0000	1988-09-19	2017-07-22	380501234573	Chernihiv	Myru Ave, 7	14000
E0008	Rudenko	Yuliia	\N	Cashier	22000.0000	1983-12-10	2012-04-03	380671234574	Vinnytsia	Soborna St, 10	21000
E0010	Lysenko	Kateryna	Borysivna	Cashier	14000.0000	1991-04-15	2022-01-10	380501234576	Ternopil	Halytska St, 14	46000
E0012	Didenko	Anastasiia	\N	Manager	17000.0000	1994-05-20	2020-10-05	380931234578	Rivne	Soborna St, 11	33000
E0013	Zaitsev	Vladyslav	Ihorovych	Cashier	30000.0000	1987-08-14	2015-12-01	380501234579	Uzhhorod	Korzo St, 4	88000
E0014	Kostenko	Mariia	\N	Cashier	16500.0000	1993-03-03	2021-09-30	380671234580	Lutsk	Volodymyrska St, 9	43000
\.


--
-- TOC entry 4904 (class 0 OID 24650)
-- Dependencies: 225
-- Data for Name: Product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Product" (id_product, category_number, product_name, characteristics) FROM stdin;
1	1	Beef Steak	High-quality beef, rich in protein
2	1	Chicken Breast	Lean white meat, low in fat
3	1	Pork Sausage	Spiced pork sausage, great for grilling
4	1	Lamb Chops	Tender lamb meat with bone
5	1	Bacon Strips	Smoked and crispy pork belly
6	2	Salmon Fillet	Rich in omega-3, fresh Atlantic salmon
7	2	Shrimp	Medium-sized shrimp, cleaned and deveined
8	2	Tuna Steak	Thick-cut tuna, ideal for grilling
9	2	Mussels	Fresh mussels, great for seafood pasta
10	2	Lobster Tail	Delicious and tender lobster meat
11	3	Cheddar Cheese	Aged yellow cheese, sharp taste
12	3	Whole Milk	Full-fat cow milk, 1L
13	3	Greek Yogurt	Thick and creamy, rich in protein
14	3	Butter	Salted creamy butter, 250g
15	3	Cottage Cheese	Low-fat, soft and crumbly cheese
16	4	Apple	Red juicy apple, rich in fiber
17	4	Carrot	Fresh organic carrot, 1kg
18	4	Banana	Sweet and ripe bananas, 6-pack
19	4	Tomato	Vine-ripened tomatoes, bright red
20	4	Broccoli	Green broccoli, rich in vitamins
21	5	Baguette	French-style bread, crispy crust
22	5	Croissant	Buttery and flaky pastry
23	5	Whole Wheat Bread	Healthy bread, rich in fiber
24	5	Donut	Classic glazed donut, soft and sweet
25	5	Muffin	Blueberry muffin, moist and tasty
26	6	Orange Juice	Freshly squeezed, no added sugar
27	6	Green Tea	Organic loose-leaf green tea
28	6	Coffee Beans	Arabica coffee beans, medium roast
29	6	Mineral Water	Still water, rich in minerals
30	6	Cola	Classic carbonated soft drink
31	7	Red Wine	Cabernet Sauvignon, aged in oak
32	7	Whiskey	12-year-old single malt scotch
33	7	Beer	Lager-style beer, crisp and refreshing
34	7	Vodka	Triple-distilled premium vodka
35	7	Rum	Dark rum, aged 5 years
\.


--
-- TOC entry 4898 (class 0 OID 24602)
-- Dependencies: 219
-- Data for Name: Sale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Sale" (product_number, selling_price, "UPC", check_number) FROM stdin;
1	2.9900	000014	CHK0001
1	14.9900	000036	CHK0001
1	5.4900	000013	CHK0001
1	10.4900	000008	CHK0001
2	39.9800	000012	CHK0001
2	2.5800	000023	CHK0001
1	9.9900	000011	CHK0002
1	8.4900	000002	CHK0002
1	2.9900	000028	CHK0002
2	19.9800	000033	CHK0002
2	31.9800	000007	CHK0002
2	4.9800	000022	CHK0002
1	1.9900	000019	CHK0003
1	18.9900	000039	CHK0003
1	2.9900	000014	CHK0003
2	1.5800	000021	CHK0003
2	31.9800	000007	CHK0003
2	16.9800	000002	CHK0003
1	5.4900	000013	CHK0004
1	15.9900	000007	CHK0004
1	5.4900	000032	CHK0004
2	45.9800	000041	CHK0004
2	9.9800	000015	CHK0004
2	20.9800	000008	CHK0004
1	10.9900	000001	CHK0005
1	19.9900	000012	CHK0005
1	9.9900	000033	CHK0005
1	5.5900	000004	CHK0005
1	1.9900	000019	CHK0005
1	14.9900	000010	CHK0005
1	9.9900	000011	CHK0006
1	18.9900	000039	CHK0006
1	3.9900	000038	CHK0006
1	15.9900	000007	CHK0006
1	5.4900	000013	CHK0006
2	25.9800	000005	CHK0006
1	3.9900	000031	CHK0007
2	1.9800	000020	CHK0007
2	21.9800	000001	CHK0007
2	15.9800	000006	CHK0007
2	11.1800	000004	CHK0007
2	20.9800	000008	CHK0007
1	9.9900	000011	CHK0008
1	3.4900	000030	CHK0008
1	7.9900	000006	CHK0008
1	5.4900	000032	CHK0008
2	7.9800	000031	CHK0008
2	3.9800	000029	CHK0008
1	9.9900	000011	CHK0009
1	6.9900	000003	CHK0009
1	1.9900	000029	CHK0009
1	4.9900	000015	CHK0009
1	5.5900	000004	CHK0009
1	1.4900	000035	CHK0009
1	24.9900	000037	CHK0010
1	1.4900	000035	CHK0010
1	4.9900	000015	CHK0010
2	4.9800	000022	CHK0010
2	37.9800	000039	CHK0010
2	1.5800	000021	CHK0010
1	1.4900	000035	CHK0011
1	3.9900	000025	CHK0011
1	1.2900	000023	CHK0011
1	5.4900	000013	CHK0011
2	15.9800	000006	CHK0011
2	5.9800	000014	CHK0011
1	10.9900	000001	CHK0012
1	3.4900	000017	CHK0012
2	7.9800	000038	CHK0012
2	49.9800	000037	CHK0012
2	6.9800	000030	CHK0012
3	5.9700	000019	CHK0012
1	2.9900	000014	CHK0013
1	8.3900	000009	CHK0013
1	14.9900	000010	CHK0013
2	37.9800	000039	CHK0013
2	6.9800	000017	CHK0013
2	19.9800	000042	CHK0013
1	2.9900	000024	CHK0014
1	9.9900	000033	CHK0014
1	15.1900	000040	CHK0014
1	1.2900	000023	CHK0014
2	15.9800	000034	CHK0014
2	45.9800	000041	CHK0014
1	1.9900	000019	CHK0015
1	3.9900	000031	CHK0015
1	4.9900	000015	CHK0015
1	2.9900	000014	CHK0015
1	3.9900	000038	CHK0015
1	4.4900	000026	CHK0015
1	24.9900	000037	CHK0016
1	3.9900	000031	CHK0016
1	3.9900	000025	CHK0016
1	3.5900	000027	CHK0016
1	8.3900	000009	CHK0016
1	4.4900	000026	CHK0016
1	3.9900	000038	CHK0017
1	1.9900	000029	CHK0017
2	7.1800	000027	CHK0017
2	16.7800	000009	CHK0017
2	13.9800	000003	CHK0017
2	31.9800	000007	CHK0017
1	5.4900	000013	CHK0018
1	3.4900	000030	CHK0018
2	30.3800	000040	CHK0018
2	21.9800	000001	CHK0018
2	49.9800	000037	CHK0018
2	7.1800	000027	CHK0018
1	6.9900	000003	CHK0019
1	9.9900	000042	CHK0019
1	3.9900	000038	CHK0019
1	15.1900	000040	CHK0019
1	1.4900	000035	CHK0019
2	16.7800	000009	CHK0019
1	5.4900	000013	CHK0020
1	12.9900	000005	CHK0020
1	1.9900	000029	CHK0020
1	9.9900	000042	CHK0020
1	5.5900	000004	CHK0020
2	29.9800	000036	CHK0020
\.


--
-- TOC entry 4899 (class 0 OID 24607)
-- Dependencies: 220
-- Data for Name: Store_Product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Store_Product" ("UPC", "UPC_prom", id_product, selling_price, products_number, promotional_product) FROM stdin;
000001	\N	1	10.9900	50	f
000002	\N	2	8.4900	40	f
000003	000004	3	6.9900	30	f
000004	\N	3	5.5900	20	t
000005	\N	4	12.9900	25	f
000006	\N	5	7.9900	35	f
000007	\N	6	15.9900	20	f
000008	000009	7	10.4900	30	f
000009	\N	7	8.3900	15	t
000010	\N	8	14.9900	25	f
000011	\N	9	9.9900	40	f
000012	\N	10	19.9900	15	f
000013	\N	11	5.4900	50	f
000014	\N	12	2.9900	60	f
000015	000016	13	4.9900	35	f
000016	\N	13	3.9900	20	t
000017	\N	14	3.4900	45	f
000018	\N	15	6.4900	30	f
000019	\N	16	1.9900	100	f
000020	000021	17	0.9900	80	f
000021	\N	17	0.7900	40	t
000022	\N	18	2.4900	90	f
000023	\N	19	1.2900	110	f
000024	\N	20	2.9900	85	f
000025	\N	21	3.9900	50	f
000026	000027	22	4.4900	40	f
000027	\N	22	3.5900	20	t
000028	\N	23	2.9900	55	f
000029	\N	24	1.9900	70	f
000030	\N	25	3.4900	45	f
000031	\N	26	3.9900	60	f
000032	\N	27	5.4900	35	f
000033	000034	28	9.9900	25	f
000034	\N	28	7.9900	10	t
000035	\N	29	1.4900	80	f
000036	\N	30	14.9900	20	f
000037	\N	31	24.9900	15	f
000038	\N	32	3.9900	50	f
000039	000040	33	18.9900	10	f
000040	\N	33	15.1900	5	t
000041	\N	34	22.9900	12	f
000042	\N	35	9.9900	30	f
\.


--
-- TOC entry 4912 (class 0 OID 0)
-- Dependencies: 222
-- Name: Category_category_number_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Category_category_number_seq"', 7, true);


--
-- TOC entry 4913 (class 0 OID 0)
-- Dependencies: 224
-- Name: Product_id_product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Product_id_product_seq"', 35, true);


--
-- TOC entry 4736 (class 2606 OID 24648)
-- Name: Category Category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category"
    ADD CONSTRAINT "Category_pkey" PRIMARY KEY (category_number);


--
-- TOC entry 4734 (class 2606 OID 24626)
-- Name: Check Check_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT "Check_pkey" PRIMARY KEY (check_number);


--
-- TOC entry 4723 (class 2606 OID 24596)
-- Name: Customer_Card Customer_Card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Customer_Card"
    ADD CONSTRAINT "Customer_Card_pkey" PRIMARY KEY (card_number);


--
-- TOC entry 4725 (class 2606 OID 24601)
-- Name: Employee Employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_pkey" PRIMARY KEY (id_employee);


--
-- TOC entry 4740 (class 2606 OID 24655)
-- Name: Product Product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT "Product_pkey" PRIMARY KEY (id_product);


--
-- TOC entry 4729 (class 2606 OID 24606)
-- Name: Sale Sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT "Sale_pkey" PRIMARY KEY ("UPC", check_number);


--
-- TOC entry 4731 (class 2606 OID 24611)
-- Name: Store_Product Store_Product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT "Store_Product_pkey" PRIMARY KEY ("UPC");


--
-- TOC entry 4738 (class 2606 OID 24679)
-- Name: Category category_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Category"
    ADD CONSTRAINT category_name UNIQUE (category_name) INCLUDE (category_name);


--
-- TOC entry 4743 (class 2606 OID 24681)
-- Name: Product product_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT product_name UNIQUE (product_name) INCLUDE (product_name);


--
-- TOC entry 4727 (class 2606 OID 24698)
-- Name: Employee zip_code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT zip_code UNIQUE (zip_code);


--
-- TOC entry 4914 (class 0 OID 0)
-- Dependencies: 4727
-- Name: CONSTRAINT zip_code ON "Employee"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT zip_code ON public."Employee" IS 'zip_code is going to be password, so it should be unique';


--
-- TOC entry 4741 (class 1259 OID 24672)
-- Name: fki_category_number; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_category_number ON public."Product" USING btree (category_number);


--
-- TOC entry 4732 (class 1259 OID 24666)
-- Name: fki_id_product; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_id_product ON public."Store_Product" USING btree (id_product);


--
-- TOC entry 4744 (class 2606 OID 24632)
-- Name: Sale UPC; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT "UPC" FOREIGN KEY ("UPC") REFERENCES public."Store_Product"("UPC") ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4746 (class 2606 OID 24673)
-- Name: Store_Product UPC_prom; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT "UPC_prom" FOREIGN KEY ("UPC_prom") REFERENCES public."Store_Product"("UPC") ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4748 (class 2606 OID 24627)
-- Name: Check card_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT card_number FOREIGN KEY (card_number) REFERENCES public."Customer_Card"(card_number) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4750 (class 2606 OID 24667)
-- Name: Product category_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Product"
    ADD CONSTRAINT category_number FOREIGN KEY (category_number) REFERENCES public."Category"(category_number) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4745 (class 2606 OID 24637)
-- Name: Sale check_number; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Sale"
    ADD CONSTRAINT check_number FOREIGN KEY (check_number) REFERENCES public."Check"(check_number) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4749 (class 2606 OID 24620)
-- Name: Check id_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Check"
    ADD CONSTRAINT id_employee FOREIGN KEY (id_employee) REFERENCES public."Employee"(id_employee) ON UPDATE CASCADE NOT VALID;


--
-- TOC entry 4747 (class 2606 OID 24661)
-- Name: Store_Product id_product; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Store_Product"
    ADD CONSTRAINT id_product FOREIGN KEY (id_product) REFERENCES public."Product"(id_product) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


-- Completed on 2025-03-24 13:47:10

--
-- PostgreSQL database dump complete
--

