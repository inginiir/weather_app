delete from weather_list;


insert into weather_list(id, city_name, date_of_request, feels_like_temperature, humidity,
pressure, source, temperature, wind_degree, wind_speed) values
( 1, 'Анадырь', '2020-07-15 16:20:19.048', 7.04, 93, 1005, 'Open Weather Map', 8.94, 90, 2),
( 2, 'Анадырь', '2020-07-15 16:25:19.048', 8.04, 90, 1001, 'Weather Bit', 8.84, 92, 4);

alter sequence hibernate_sequence restart with 3;

