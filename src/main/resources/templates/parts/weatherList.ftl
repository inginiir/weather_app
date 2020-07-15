<div class="card-columns" id="weather-list">
    <#if weatherList??>
        <#list weatherList as note>
            <div class="card my-3" data-id="${note.id}">
                <div class="m-2">
                    <h5>${note.getCityName()}</h5>
                    <p>Дата и время запроса: ${note.getDateOfRequest()?datetime}</p>
                    <p>Температура: ${note.getTemperature()}&#x00B0;C</p>
                    <p>Температура по ощущению: ${note.getFeelsLikeTemperature()}&#x00B0;C</p>
                    <p>Давление: ${note.getPressure()/1.33} мм.рт.столба</p>
                    <p>Влажность: ${note.getHumidity()}%</p>
                    <p>
                        Направление ветра:
                        <#if note.getWindDegree() gte 22.5 && note.getWindDegree() lt 67.5>
                            С-В
                        <#elseif note.getWindDegree() gte 67.5 && note.getWindDegree() lt 112.5>
                            В
                        <#elseif note.getWindDegree() gte 112.5 && note.getWindDegree() lt 157.5>
                            Ю-В
                        <#elseif note.getWindDegree() gte 157.5 && note.getWindDegree() lt 202.5>
                            Ю
                        <#elseif note.getWindDegree() gte 202.5 && note.getWindDegree() lt 247.5>
                            Ю-З
                        <#elseif note.getWindDegree() gte 247.5 && note.getWindDegree() lt 292.5>
                            З
                        <#elseif note.getWindDegree() gte 292.5 && note.getWindDegree() lt 337.5>
                            С-З
                        <#else>
                            С
                        </#if>
                    </p>
                    <p>Скорость ветра: ${note.getWindSpeed()} м/с</p>
                    <p>Источник: ${note.getSource()}</p>
                </div>
                <div class="card-footer text-muted continer">
                    <div class="row">
                        <a class="col align-self-center" href="/${note.id}" title="Удалить запись">
                            <i class="fas fa-trash-alt"></i>
                        </a>
                    </div>
                </div>
            </div>
        <#else>
            No notes
        </#list>
    </#if>
</div>