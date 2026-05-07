let currentAlertId = null;

const TYPE_COLOURS = {
    ENTERTAINMENT:  '#6f42c1',
    FOOD_AND_DRINK: '#fd7e14',
    WELFARE:        '#20c997',
    FACILITIES:     '#0dcaf0',
    RETAIL:         '#e83e8c',
};
const TYPE_SYMBOLS = {
    ENTERTAINMENT:  'fa-music',
    FOOD_AND_DRINK: 'fa-utensils',
    WELFARE:        'fa-heart',
    FACILITIES:     'fa-restroom',
    RETAIL:         'fa-bag-shopping',
};
function typeColour(type) { return TYPE_COLOURS[type] || '#6c757d'; }
function typeSymbol(type) { return TYPE_SYMBOLS[type] || 'fa-location-dot'; }

document.addEventListener('DOMContentLoaded', () => {
    loadSummary();
    loadAlerts();
    loadReports();
    loadAreas();
    loadAreaStatus();
    initMap();

    setInterval(() => { loadAlerts(); loadSummary(); }, 3000);
    setInterval(() => { loadReports(); loadAreaStatus(); }, 30000);

    document.getElementById('confirmResolveBtn').addEventListener('click', async () => {
        if (!currentAlertId) return;
        const level = document.querySelector('input[name="resolveLevel"]:checked').value;
        try {
            const res = await fetch(`/api/alerts/${currentAlertId}/resolve?newLevel=${level}`, { method: 'POST' });
            if (res.ok) {
                bootstrap.Modal.getInstance(document.getElementById('resolveModal')).hide();
                loadAlerts(); loadSummary(); loadReports(); loadAreaStatus();
            }
        } catch (e) { console.error(e); }
    });

    document.getElementById('map-container')?.addEventListener('click', (e) => {
        if (!e.target.closest('.area-marker-wrap')) closePopup();
    });
});

// ── Summary ───────────────────────────────────────────────────────────────────

async function loadSummary() {
    try {
        const data = await fetch('/api/dashboard/summary').then(r => r.json());
        document.getElementById('stat-areas').textContent = data.totalAreas;
        document.getElementById('stat-reports').textContent = data.totalReports;
        document.getElementById('stat-alerts').textContent = data.activeAlerts;
    } catch (e) { console.error(e); }
}

// ── Alerts ────────────────────────────────────────────────────────────────────

async function loadAlerts() {
    try {
        const alerts = await fetch('/api/alerts').then(r => r.json());
        const container = document.getElementById('alerts-list');
        container.innerHTML = '';
        if (!alerts.length) {
            container.innerHTML = '<div class="no-alerts"><i class="fa-solid fa-circle-check"></i>All clear — no active alerts</div>';
            return;
        }
        alerts.forEach(alert => {
            const div = document.createElement('div');
            div.className = 'alert-item';
            div.innerHTML = `
                <div>
                    <h6>${alert.message}</h6>
                    <small>${alert.area.name} &nbsp;·&nbsp; ${new Date(alert.timestamp).toLocaleString()}</small>
                </div>
                <button class="fp-btn fp-btn-danger fp-btn-sm" onclick="showResolveModal(${alert.id}, '${alert.area.name.replace(/'/g, "\\'")}')">
                    <i class="fa-solid fa-check"></i> Resolve
                </button>`;
            container.appendChild(div);
        });
    } catch (e) { console.error(e); }
}

function showResolveModal(id, areaName) {
    currentAlertId = id;
    document.getElementById('modal-area-name').textContent = areaName;
    new bootstrap.Modal(document.getElementById('resolveModal')).show();
}

// ── Reports ───────────────────────────────────────────────────────────────────

async function loadReports() {
    try {
        const reports = await fetch('/api/reports').then(r => r.json());
        const tbody = document.getElementById('reports-list');
        tbody.innerHTML = '';
        reports.forEach(r => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${new Date(r.timestamp).toLocaleTimeString()}</td>
                <td>${r.area.name}</td>
                <td><span class="badge ${getBadgeClass(r.crowdLevel)}">${r.crowdLevel}</span></td>
                <td>${r.shortNote || ''}</td>`;
            tbody.appendChild(tr);
        });
    } catch (e) { console.error(e); }
}

function getBadgeClass(level) {
    return 'lvl-badge lvl-' + (level || 'UNKNOWN');
}

// ── Area Status ───────────────────────────────────────────────────────────────

async function loadAreaStatus() {
    try {
        const areas = await fetch('/api/areas/status').then(r => r.json());
        const container = document.getElementById('areas-summary-list');
        container.innerHTML = '';
        areas.forEach(area => {
            const div = document.createElement('div');
            div.className = 'area-status-card';
            div.innerHTML = `
                <div>
                    <div class="area-name">${area.name}</div>
                    <div class="area-time">${area.latestTime ? new Date(area.latestTime).toLocaleTimeString() : 'No data'}</div>
                </div>
                <span class="${getBadgeClass(area.latestLevel)}">${area.latestLevel || 'UNKNOWN'}</span>`;
            container.appendChild(div);
        });
        renderMapMarkers(areas);
    } catch (e) { console.error(e); }
}

// ── Areas ─────────────────────────────────────────────────────────────────────

async function loadAreas() {
    try {
        const areas = await fetch('/api/areas').then(r => r.json());

        const reportSelect = document.getElementById('report-area');
        reportSelect.innerHTML = '<option value="">Select Area...</option>';

        const mapSelect = document.getElementById('map-area-select');
        mapSelect.innerHTML = '<option value="">Select area...</option>';

        const list = document.getElementById('all-areas-list');
        list.innerHTML = '';

        areas.forEach(area => {
            [reportSelect, mapSelect].forEach(sel => {
                const opt = document.createElement('option');
                opt.value = area.id;
                opt.textContent = area.name;
                sel.appendChild(opt);
            });

            const li = document.createElement('div');
            li.className = 'area-list-item';
            li.innerHTML = `
                <div class="area-type-dot" style="background:${typeColour(area.type)}"></div>
                <div>
                    <div style="font-size:0.88rem;font-weight:600">${area.name}</div>
                    <div class="area-desc">${area.description}</div>
                </div>
                <span class="type-chip" style="background:${typeColour(area.type)}22;color:${typeColour(area.type)};border:1px solid ${typeColour(area.type)}44">${area.type.replace(/_/g,' ')}</span>`;
            list.appendChild(li);
        });
    } catch (e) { console.error(e); }
}

document.getElementById('area-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const area = {
        name: document.getElementById('area-name').value,
        description: document.getElementById('area-desc').value,
        type: document.getElementById('area-type').value
    };
    try {
        const res = await fetch('/api/areas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(area)
        });
        if (res.ok) {
            document.getElementById('area-form').reset();
            loadAreas(); loadAreaStatus(); loadSummary();
        } else {
            alert('Error: ' + await res.text());
        }
    } catch (e) { console.error(e); }
});

document.getElementById('report-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const areaId = document.getElementById('report-area').value;
    if (!areaId) return alert('Please select an area');
    const report = {
        area: { id: areaId },
        crowdLevel: document.getElementById('report-level').value,
        shortNote: document.getElementById('report-note').value
    };
    try {
        const res = await fetch('/api/reports', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(report)
        });
        if (res.ok) {
            document.getElementById('report-form').reset();
            loadReports(); loadAlerts(); loadSummary(); loadAreaStatus();
        } else {
            alert('Error: ' + await res.text());
        }
    } catch (e) { console.error(e); }
});

// ── Map ───────────────────────────────────────────────────────────────────────

function initMap() {
    const mapImg = document.getElementById('map-img');
    const xInput = document.getElementById('map-x');
    const yInput = document.getElementById('map-y');

    mapImg.addEventListener('click', (e) => {
        const rect = mapImg.getBoundingClientRect();
        const x = ((e.clientX - rect.left) / rect.width * 100).toFixed(1);
        const y = ((e.clientY - rect.top) / rect.height * 100).toFixed(1);
        xInput.value = x;
        yInput.value = y;
        movePendingMarker(x, y);
        document.getElementById('map-click-hint').textContent = `Selected: X=${x}%, Y=${y}%`;
    });

    [xInput, yInput].forEach(input => {
        input.addEventListener('input', () => {
            const x = parseFloat(xInput.value), y = parseFloat(yInput.value);
            if (!isNaN(x) && !isNaN(y)) movePendingMarker(x, y);
        });
    });

    document.getElementById('map-place-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const areaId = document.getElementById('map-area-select').value;
        if (!areaId) return alert('Select an area first');
        const x = parseFloat(xInput.value), y = parseFloat(yInput.value);
        if (isNaN(x) || isNaN(y)) return alert('Enter or click to set coordinates');
        try {
            const res = await fetch(`/api/areas/${areaId}/location`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ locationX: x, locationY: y })
            });
            if (res.ok) {
                document.getElementById('pending-marker').style.display = 'none';
                document.getElementById('map-click-hint').textContent = 'Click map to select coordinates';
                loadAreaStatus();
            } else {
                alert('Error: ' + await res.text());
            }
        } catch (e) { console.error(e); }
    });
}

function movePendingMarker(x, y) {
    const m = document.getElementById('pending-marker');
    m.style.left = x + '%';
    m.style.top = y + '%';
    m.style.display = 'block';
}

function renderMapMarkers(areas) {
    document.querySelectorAll('.area-marker-wrap, .marker-popup').forEach(el => el.remove());

    const container = document.getElementById('map-container');
    if (!container) return;

    areas.forEach(area => {
        if (area.locationX == null || area.locationY == null) return;

        const wrap = document.createElement('div');
        wrap.className = 'area-marker-wrap';
        wrap.style.left = area.locationX + '%';
        wrap.style.top = area.locationY + '%';

        // Crowd density ring
        const level = area.latestLevel;
        if (level === 'FULL' || level === 'MEDIUM') {
            const ring = document.createElement('div');
            ring.className = `crowd-ring ${level === 'FULL' ? 'full' : 'medium'}`;
            wrap.appendChild(ring);
        }

        const marker = document.createElement('div');
        marker.className = 'area-marker';
        marker.style.background = typeColour(area.type);
        marker.innerHTML = `<i class="fa-solid ${typeSymbol(area.type)}" style="color:#fff;font-size:13px;pointer-events:none"></i>`;

        wrap.appendChild(marker);
        wrap.addEventListener('click', (e) => {
            e.stopPropagation();
            document.getElementById('map-area-select').value = area.id;
            document.getElementById('map-x').value = area.locationX;
            document.getElementById('map-y').value = area.locationY;
            movePendingMarker(area.locationX, area.locationY);
            showPopup(area, wrap);
        });

        container.appendChild(wrap);
    });
}

function showPopup(area, wrapEl) {
    closePopup();
    const container = document.getElementById('map-container');
    const popup = document.createElement('div');
    popup.className = 'marker-popup';
    popup.id = 'active-popup';

    const updated = area.latestTime ? new Date(area.latestTime).toLocaleTimeString() : 'N/A';
    popup.innerHTML = `
        <span class="close-popup" onclick="closePopup()">×</span>
        <h6>${area.name}</h6>
        <div><span class="type-chip" style="background:${typeColour(area.type)}22;color:${typeColour(area.type)};border:1px solid ${typeColour(area.type)}44">${area.type.replace(/_/g, ' ')}</span></div>
        <div class="mt-2">Crowd: <span class="${getBadgeClass(area.latestLevel)}">${area.latestLevel || 'UNKNOWN'}</span></div>
        <div style="font-size:11px;color:var(--muted);margin-top:4px">Updated: ${updated}</div>`;

    const x = parseFloat(wrapEl.style.left);
    const y = parseFloat(wrapEl.style.top);
    popup.style.top = `calc(${y}% - 10px)`;
    popup.style.left = x > 65 ? `calc(${x}% - 185px)` : `calc(${x}% + 14px)`;

    container.appendChild(popup);
}

function closePopup() {
    document.getElementById('active-popup')?.remove();
}
