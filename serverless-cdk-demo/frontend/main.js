function fetchFoundationModel(modelId) {
    const apiEndpoint = frontendConfig.apiDomain + 'foundation-models/' + modelId;
    fetch(apiEndpoint)
        .then((response) => response.json())
        .then((data) => {
            const table = document.getElementById('foundationModels');
            table.innerHTML = '';

            providerRow = document.createElement('tr');
            providerKey = document.createElement('td');
            providerKey.textContent = 'Provider:';
            providerValue = document.createElement('td');
            providerValue.textContent = data.providerName;
            providerRow.appendChild(providerKey);
            providerRow.appendChild(providerValue);
            table.appendChild(providerRow);

            nameRow = document.createElement('tr');
            nameKey = document.createElement('td');
            nameKey.textContent = 'Model Name:';
            nameValue = document.createElement('td');
            nameValue.textContent = data.modelName;
            nameRow.appendChild(nameKey);
            nameRow.appendChild(nameValue);
            table.appendChild(nameRow);

            idRow = document.createElement('tr');
            idKey = document.createElement('td');
            idKey.textContent = 'Model ID:';
            idValue = document.createElement('td');
            idValue.textContent = data.modelId;
            idRow.appendChild(idKey);
            idRow.appendChild(idValue);
            table.appendChild(idRow);
        });
}

function listFoundationModels() {
    console.log('listFoundationModels()');

    const apiEndpoint = frontendConfig.apiDomain + 'foundation-models';

    fetch(apiEndpoint)
        .then((response) => response.json())
        .then((data) => {
            const table = document.getElementById('foundationModels');
            table.innerHTML = '';

            data.foundationModels.forEach((item) => {
                console.log(item);
                const row = document.createElement('tr');

                const provider = document.createElement('td');
                provider.textContent = item.provider;
                row.appendChild(provider);

                const name = document.createElement('td');
                name.textContent = item.modelName;
                row.appendChild(name);

                row.onclick = function () {
                    console.log('getFoundationModel(' + item.modelId + ')');
                    fetchFoundationModel(item.modelId);
                };

                row.style.cursor = 'pointer';

                table.appendChild(row);
            });
        });
}
