// static/js/product-form.js

document.addEventListener("DOMContentLoaded", function () {
    new TomSelect("#shopSelect", {
        create: false,
        maxItems: 1,
        placeholder: '가게 이름을 검색하세요',
        allowEmptyOption: false,
        persist: false,
        searchField: ['text'],
        sortField: {
            field: 'text',
            direction: 'asc'
        }
    });
});
