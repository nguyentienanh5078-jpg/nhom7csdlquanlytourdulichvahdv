// =========================================================
// CẤU HÌNH DATABASE
// =========================================================
const dbSchema = {
    CUSTOMER: { name: 'Khách Hàng', icon: 'bi-people', cols: ['CID', 'CName', 'CAddress', 'CPN'], labels: ['Mã KH', 'Tên Khách Hàng', 'Địa Chỉ', 'Số Điện Thoại'] },
    HOTEL: { name: 'Khách Sạn', icon: 'bi-building', cols: ['HID', 'HName', 'CIDate', 'CODate', 'HAddress'], labels: ['Mã KS', 'Tên Khách Sạn', 'Ngày Check-in', 'Ngày Check-out', 'Địa Chỉ KS'] },
    TOUR: { name: 'Chuyến Tour', icon: 'bi-map', cols: ['TID', 'TName', 'Price', 'SDate', 'EDate', 'HID'], labels: ['Mã Tour', 'Tên Tour', 'Giá Tour', 'Ngày Đi', 'Ngày Về', 'Mã Khách Sạn'] },
    AGENCY: { name: 'Đại Lý', icon: 'bi-shop', cols: ['AID', 'APN', 'AAddress'], labels: ['Mã Đại Lý', 'SĐT Đại Lý', 'Địa Chỉ'] },
    TOURGUIDE: { name: 'Hướng Dẫn Viên', icon: 'bi-person-badge', cols: ['TGID', 'TGName', 'TGPN', 'AID'], labels: ['Mã HDV', 'Tên HDV', 'Số Điện Thoại', 'Mã Đại Lý'] },
    SALARY: { name: 'Lương HDV', icon: 'bi-cash-coin', cols: ['SIDTG', 'TGS', 'TGID'], labels: ['Mã Lương', 'Mức Lương', 'Mã HDV'] },
    RECEIPT: { name: 'Hóa Đơn', icon: 'bi-receipt', cols: ['RID', 'RDate', 'SumR', 'CID', 'AID'], labels: ['Mã Hóa Đơn', 'Ngày Lập', 'Tổng Tiền', 'Mã KH', 'Mã Đại Lý'] },
    VEHICLE: { name: 'Phương Tiện', icon: 'bi-car-front', cols: ['LicensePlate', 'VType'], labels: ['Biển Số Xe', 'Loại Xe'] },
    GOBY: { name: 'Tour - Xe (GOBY)', icon: 'bi-link', cols: ['TID', 'LicensePlate'], labels: ['Mã Tour', 'Biển Số Xe'] },
    TLOCATION: { name: 'Địa Điểm', icon: 'bi-geo-alt', cols: ['LID', 'LName', 'Province'], labels: ['Mã Địa Điểm', 'Tên Địa Điểm', 'Tỉnh/Thành'] },
    COME: { name: 'Tour - Địa điểm', icon: 'bi-link', cols: ['TID', 'LID'], labels: ['Mã Tour', 'Mã Địa Điểm'] },
    ASSIGN: { name: 'Phân công HDV', icon: 'bi-link', cols: ['TID', 'TGID'], labels: ['Mã Tour', 'Mã HDV'] },
    BOOKING: { name: 'Đặt Tour', icon: 'bi-journal-check', cols: ['TID', 'CID'], labels: ['Mã Tour', 'Mã KH'] }
};

let currentTable = 'CUSTOMER'; 
let dbData = {};
let editingIndex = -1; // -1 nghĩa là đang Thêm mới. Nếu >= 0 là đang Sửa.

for (const table in dbSchema) {
    dbData[table] = JSON.parse(localStorage.getItem(table)) || [];
}

function initApp() { renderSidebar(); loadTable(currentTable); }

function renderSidebar() {
    const menu = document.getElementById('sidebarMenu');
    menu.innerHTML = Object.keys(dbSchema).map(table => `
        <a class="nav-link d-block ${table === currentTable ? 'active' : ''}" onclick="changeTable('${table}')">
            <i class="bi ${dbSchema[table].icon} me-2"></i> ${dbSchema[table].name}
        </a>
    `).join('');
}

window.changeTable = function(tableName) {
    currentTable = tableName;
    renderSidebar();
    loadTable(tableName);
};

function loadTable(tableName) {
    const config = dbSchema[tableName];
    document.getElementById('pageTitle').innerText = `Quản lý ${config.name}`;
    document.getElementById('modalTitle').innerText = `Thêm mới ${config.name}`;
    
    document.getElementById('tableHeader').innerHTML = config.labels.map(lbl => `<th>${lbl}</th>`).join('') + `<th>Thao tác</th>`;
    
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = dbData[tableName].map((row, index) => {
        const trData = config.cols.map(col => `<td>${row[col] || ''}</td>`).join('');
        // Đã thêm nút Sửa (Màu vàng)
        const actions = `
            <td>
                <button class="btn btn-sm btn-warning text-dark me-1" onclick="editRecord('${tableName}', ${index})"><i class="bi bi-pencil-square"></i></button>
                <button class="btn btn-sm btn-danger" onclick="deleteRecord('${tableName}', ${index})"><i class="bi bi-trash"></i></button>
            </td>
        `;
        return `<tr>${trData}${actions}</tr>`;
    }).join('');

    const formInputs = document.getElementById('formInputs');
    formInputs.innerHTML = config.cols.map((col, idx) => `
        <div class="mb-2">
            <label class="form-label fw-bold">${config.labels[idx]} (${col})</label>
            <input type="text" class="form-control" id="input_${col}" required>
        </div>
    `).join('');
}

// =========================================================
// XỬ LÝ NÚT BẤM (THÊM / SỬA / XÓA)
// =========================================================

// Khi bấm nút "Thêm Mới" trên góc phải, reset lại form
document.getElementById('btnAdd') && document.getElementById('btnAdd').addEventListener('click', () => {
    editingIndex = -1; // Chuyển về chế độ Thêm mới
    document.getElementById('dataForm').reset();
    document.getElementById('modalTitle').innerText = `Thêm mới ${dbSchema[currentTable].name}`;
});

// Hàm gọi ra khi bấm nút Sửa
window.editRecord = function(tableName, index) {
    editingIndex = index; // Lưu lại vị trí đang sửa
    const config = dbSchema[tableName];
    const record = dbData[tableName][index];

    document.getElementById('modalTitle').innerText = `Sửa dữ liệu ${config.name}`;

    // Đổ dữ liệu cũ vào các ô input
    config.cols.forEach(col => {
        document.getElementById(`input_${col}`).value = record[col];
    });

    // Mở Modal lên
    new bootstrap.Modal(document.getElementById('dataModal')).show();
};

// Form Submit: Xử lý cả Thêm mới và Cập nhật
document.getElementById('dataForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const config = dbSchema[currentTable];
    const newRecord = {};
    config.cols.forEach(col => { newRecord[col] = document.getElementById(`input_${col}`).value; });
    
    if (editingIndex === -1) {
        // Nếu là -1 -> Thêm mới
        dbData[currentTable].push(newRecord);
    } else {
        // Nếu >= 0 -> Cập nhật ghi đè
        dbData[currentTable][editingIndex] = newRecord;
        editingIndex = -1; // Reset lại
    }

    localStorage.setItem(currentTable, JSON.stringify(dbData[currentTable]));
    loadTable(currentTable);
    
    const modalInstance = bootstrap.Modal.getInstance(document.getElementById('dataModal'));
    if(modalInstance) modalInstance.hide();
});

window.deleteRecord = function(tableName, index) {
    if(confirm('Xác nhận xóa bản ghi này?')) {
        dbData[tableName].splice(index, 1);
        localStorage.setItem(tableName, JSON.stringify(dbData[tableName]));
        loadTable(tableName);
    }
};

// =========================================================
// TRA CỨU LIÊN KẾT & NHẬP FILE
// =========================================================
window.searchCustomerInfo = function() {
    const phone = document.getElementById('searchPhone').value.trim();
    if(!phone) return alert("Vui lòng nhập số điện thoại!");

    const customer = dbData['CUSTOMER'].find(c => c.CPN === phone);
    if(!customer) return alert("Không tìm thấy khách hàng với SĐT này!");

    const bookings = dbData['BOOKING'].filter(b => b.CID === customer.CID);
    let html = `<h4>Khách hàng: <span class="text-primary">${customer.CName}</span> (Mã: ${customer.CID})</h4><hr>`;
    
    if(bookings.length === 0) {
        html += `<p class="text-danger">Khách hàng này chưa đặt tour nào.</p>`;
    } else {
        bookings.forEach((booking, idx) => {
            const tour = dbData['TOUR'].find(t => t.TID === booking.TID);
            if(tour) {
                const hotel = dbData['HOTEL'].find(h => h.HID === tour.HID);
                const goby = dbData['GOBY'].find(g => g.TID === tour.TID);
                const vehicle = goby ? dbData['VEHICLE'].find(v => v.LicensePlate === goby.LicensePlate) : null;
                const assign = dbData['ASSIGN'].find(a => a.TID === tour.TID);
                const guide = assign ? dbData['TOURGUIDE'].find(tg => tg.TGID === assign.TGID) : null;

                html += `
                    <div class="alert alert-success">
                        <h5>${idx + 1}. Tour: ${tour.TName} (Mã: ${tour.TID})</h5>
                        <ul class="mb-0">
                            <li><strong>Thời gian:</strong> ${tour.SDate} đến ${tour.EDate}</li>
                            <li><strong>Khách sạn:</strong> ${hotel ? hotel.HName + ' - ' + hotel.HAddress : 'Chưa có thông tin'}</li>
                            <li><strong>Phương tiện:</strong> ${vehicle ? vehicle.VType + ' (Biển số: ' + vehicle.LicensePlate + ')' : 'Chưa xếp xe'}</li>
                            <li><strong>HDV:</strong> ${guide ? guide.TGName + ' (SĐT: ' + guide.TGPN + ')' : 'Chưa phân công'}</li>
                        </ul>
                    </div>`;
            }
        });
    }

    document.getElementById('searchResultContent').innerHTML = html;
    new bootstrap.Modal(document.getElementById('searchResultModal')).show();
};

window.importData = function(event) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function(e) {
        try {
            const importedArray = JSON.parse(e.target.result);
            if(Array.isArray(importedArray)) {
                dbData[currentTable] = dbData[currentTable].concat(importedArray);
                localStorage.setItem(currentTable, JSON.stringify(dbData[currentTable]));
                loadTable(currentTable);
                alert(`Đã nhập thành công ${importedArray.length} bản ghi vào bảng ${currentTable}!`);
            } else {
                alert("File không đúng định dạng. Cần mảng JSON: [ {cột1: giá trị}, ... ]");
            }
        } catch (error) {
            alert("Lỗi đọc file: " + error.message);
        }
        event.target.value = ''; 
    };
    reader.readAsText(file);
};

initApp();