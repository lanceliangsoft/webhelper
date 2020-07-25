//--- data models ---

<#list models as model>
export type ${model.tsType} = {
<#list model.properties as prop>
    ${prop.name} : ${prop.type};
</#list>
}
<#sep>

</#sep>
</#list>

//--- REST services ---

<#list services as service>
export const ${service.name} = {
<#list service.endpoints as ep>
    async ${ep.name}(<#list ep.params as p>${p.name}: ${p.type}<#sep>, </#list>) : Promise<${ep.returnType}> {
        const response = await window.fetch(${ep.url}<#if ep.method != 'GET'>, {
            method: "${ep.method}"<#if ep.requestBody??>,
            body: ${ep.requestBody}</#if>
        }</#if>);
        return await response.json();
    }<#sep>,

</#sep>
</#list>
}
<#sep>

</#sep>
</#list>