document.addEventListener('DOMContentLoaded', () => {
    loadSummary();
    loadAlerts();
    loadReports();
    loadAreas();

    // Refresh every 30 seconds
    setInterval(() => {
        loadSummary();
        loadAlerts();
        loadReports();
    }, 30000);
});

async function loadSummary() {
    try {
        const response = await fetch('/api/dashboard/summary');
        const data = await response.json();
        document.getElementById('stat-areas').textContent = data.totalAreas;
        document.getElementById('stat-reports').textContent = data.totalReports;
        document.getElementById('stat-alerts').textContent = data.activeAlerts;
    } catch (e) { console.error('Error loading summary', e); }
}

async function loadAlerts() {
    try {
        const response = await fetch('/api/alerts');
        const alerts = await response.json();
        const container = document.getElementById('alerts-list');
        container.innerHTML = '';
        
        if (alerts.length === 0) {
            container.innerHTML = '<div class="alert alert-success">No active alerts.</div>';
            return;
        }

        alerts.forEach(alert => {
            const div = document.createElement('div');
            div.className = 'card alert-item';
            div.innerHTML = `
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h5 class="card-title">${alert.message}</h5>
                        <p class="card-text text-muted">Area: ${alert.area.name} | Created: ${new Date(alert.timestamp).toLocaleString()}</p>
                    </div>
                    <button class="btn btn-outline-danger" onclick="resolveAlert(${alert.id})">Resolve</button>
                </div>
            `;
            container.appendChild(div);
        });
    } catch (e) { console.error('Error loading alerts', e); }
}

async function resolveAlert(id) {
    try {
        const response = await fetch(`/api/alerts/${id}/resolve`, { method: 'POST' });
        if (response.ok) {
            loadAlerts();
            loadSummary();
        }
    } catch (e) { console.error('Error resolving alert', e); }
}

async function loadReports() {
    try {
        const response = await fetch('/api/reports');
        const reports = await response.json();
        const tbody = document.getElementById('reports-list');
        tbody.innerHTML = '';
        
        reports.forEach(report => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${new Date(report.timestamp).toLocaleTimeString()}</td>
                <td>${report.area.name}</td>
                <td><span class="badge ${getBadgeClass(report.crowdLevel)}">${report.crowdLevel}</span></td>
                <td>${report.note || ''}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (e) { console.error('Error loading reports', e); }
}

function getBadgeClass(level) {
    if (level === 'LOW') return 'bg-success';
    if (level === 'MEDIUM') return 'bg-warning';
    return 'bg-danger';
}

async function loadAreas() {
    try {
        const response = await fetch('/api/areas');
        const areas = await response.json();
        
        // Populate area select in report form
        const select = document.getElementById('report-area');
        select.innerHTML = '<option value="">Select Area...</option>';
        
        // Populate area list in areas tab
        const list = document.getElementById('all-areas-list');
        list.innerHTML = '';

        areas.forEach(area => {
            const option = document.createElement('option');
            option.value = area.id;
            option.textContent = area.name;
            select.appendChild(option);

            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.innerHTML = `<strong>${area.name}</strong> (${area.type})<br><small>${area.description}</small>`;
            list.appendChild(li);
        });
    } catch (e) { console.error('Error loading areas', e); }
}

document.getElementById('area-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const area = {
        name: document.getElementById('area-name').value,
        description: document.getElementById('area-desc').value,
        type: document.getElementById('area-type').value
    };

    try {
        const response = await fetch('/api/areas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(area)
        });
        if (response.ok) {
            document.getElementById('area-form').reset();
            loadAreas();
            loadSummary();
            alert('Area created successfully!');
        }
    } catch (e) { console.error('Error creating area', e); }
});

document.getElementById('report-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const areaId = document.getElementById('report-area').value;
    if (!areaId) return alert('Please select an area');

    const report = {
        area: { id: areaId },
        crowdLevel: document.getElementById('report-level').value,
        note: document.getElementById('report-note').value
    };

    try {
        const response = await fetch('/api/reports', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(report)
        });
        if (response.ok) {
            document.getElementById('report-form').reset();
            loadReports();
            loadAlerts();
            loadSummary();
            alert('Report submitted successfully!');
        } else {
            const msg = await response.text();
            alert('Error: ' + msg);
        }
    } catch (e) { console.error('Error submitting report', e); }
});
