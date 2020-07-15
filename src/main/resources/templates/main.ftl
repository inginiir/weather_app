<#import "parts/common.ftl" as c>

<@c.page>
    <div class="opacity alert alert-primary" role="alert">
        Данный погодный сервис позволяет искать и просматривать данные о погоде. Для начала работы выберите город из
        списка и один из двух сервисов предоставляющих данные о погоде.
        <#if citiesError??>
            <div class="invalid-feedback">
                ${citiesError}
            </div>
        </#if>
    </div>
    <a class="btn btn-primary mb-2" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Запрос погодных данных
    </a>
    <div class="collapse <#if weatherList??>show</#if>" id="collapseExample">
        <div class="form-group mt-3">
            <form method="post">
                <div class="form-group" ID="items">
                    <input class="form-control ${(cityError??)?string('is-invalid', '')}"
                           name="city"
                           list="citiesList" placeholder="Выберите город" maxlength="100"/>
                    <datalist id="citiesList">
                        <#if cities??>
                            <#list cities as city>
                                <option value="<#if city??>${city.name}<#else></#if>"/>
                                <br></br>
                            </#list>
                        </#if>
                    </datalist>
                    <#if cityError??>
                        <div class="invalid-feedback" id="cityErr">
                            <div>${cityError}</div>
                        </div>
                    </#if>
                    <select class="form-control mb-3 mt-3" name="weatherService"
                            title="Выберите сервис, предоставляющий погодные данные">
                        <option>Open Weather Map</option>
                        <option>Weather Bit</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Запрос</button>
                </div>
            </form>
        </div>
    </div>
    <#include "parts/weatherList.ftl"/>
</@c.page>