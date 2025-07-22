const API_URL = 'http://localhost:4000/api/v1/tasks';

const taskForm = document.getElementById('taskForm');
const taskList = document.getElementById('taskList');
const editForm = document.getElementById('editForm');
const errorContainer = document.getElementById("errorContainer");
const errorEditContainer = document.getElementById("errorEditContainer");
const dueDateInput = document.getElementById("dueDate");

const options = { timeZone: "Europe/London", weekday: "long", year: "numeric", month: "long", day: "numeric", hour: "2-digit", minute: "2-digit" };

const now = new Date();
now.setMinutes(now.getMinutes() - now.getTimezoneOffset() + 10);
dueDateInput.min = now.toISOString().slice(0, 16);

const toLocalISOString = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
};

async function fetchTasks() {
    const res = await axios.get(API_URL);
    taskList.innerHTML = '';
    res.data.forEach(task => addTaskToDOM(task));
}

function addTaskToDOM(task) {
    const div = document.createElement('div');
    div.className = 'task';
    const formattedDate = new Date(task.dueDateTime).toLocaleString("en-GB", options);

    div.innerHTML = `
    <strong>${task.title}</strong>
    <p>${task.description}</p>
    <p>Status:
      <select id="status-${task.id}" name="status" data-id="${task.id}" class="statusSelect">
        <option value="TODO" ${task.status === 'TODO' ? 'selected' : ''}>📝 To Do</option>
        <option value="IN_PROGRESS" ${task.status === 'IN_PROGRESS' ? 'selected' : ''}>⚡ In Progress</option>
        <option value="DONE" ${task.status === 'DONE' ? 'selected' : ''}>✅ Done</option>
      </select>
    </p>
    <!-- <p>Due: ${new Date(task.dueDateTime).toString()}</p> -->
    <p>Due: ${formattedDate}</p>
    <button onclick="editTask(${task.id})">Edit</button>
    <button onclick="deleteTask(${task.id})" class="delete-btn">Delete</button>
  `;
    taskList.appendChild(div);
}

taskForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const localDateTimeValue = document.getElementById('dueDate').value;

    const data = {
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        status: document.getElementById('status').value,
        dueDateTime: new Date(localDateTimeValue).toISOString()
    };
    try {
        await axios.post(API_URL, data);
        taskForm.reset();
        errorContainer.innerHTML = "";
        fetchTasks();
    } catch (error) {
        const apiErrors = error.response?.data?.errors;
        const errorMessages = apiErrors ? Object.values(apiErrors) : [];
        if (errorMessages.length > 0) {
            errorContainer.innerHTML = errorMessages.map(msg => `<p>${msg}</p>`).join("");
        }
        console.error("API Error on creating task:", error.response ? error.response.data : error.message);
    }
});

async function editTask(id) {
    const res = await axios.get(`${API_URL}/${id}`);
    const task = res.data;
    document.getElementById('editTaskId').value = task.id;
    document.getElementById('editTitle').value = task.title;
    document.getElementById('editTitle').setAttribute('maxlength', '50');

    document.getElementById('editDescription').value = task.description;
    document.getElementById('editDescription').setAttribute('maxlength', '200');

    document.getElementById('editStatus').value = task.status;

    const now = new Date();
    now.setTime(now.getTime() + 60 * 10 * 1000);
    document.getElementById("editDueDate").min = toLocalISOString(now);
    document.getElementById('editDueDate').value = toLocalISOString(new Date(task.dueDateTime));

    document.getElementById('editPopover').style.display = 'block';
    document.getElementById('editTitle').focus();
}

editForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('editTaskId').value;
    const data = {
        title: document.getElementById('editTitle').value,
        description: document.getElementById('editDescription').value,
        status: document.getElementById('editStatus').value,
        dueDateTime: new Date(document.getElementById('editDueDate').value).toISOString()
    };
    try {
        await axios.put(`${API_URL}/${id}`, data);
        document.getElementById('editPopover').style.display = 'none';
        errorEditContainer.innerHTML = "";
        fetchTasks();
    } catch (error) {
        const apiErrors = error.response?.data?.errors;
        const errorMessages = apiErrors ? Object.values(apiErrors) : [];
        if (errorMessages.length > 0) {
            errorEditContainer.innerHTML = errorMessages.map(msg => `<p>${msg}</p>`).join("");
        }
        console.error("API Error on edit:", error.response ? error.response.data : error.message);
    }
});

document.getElementById('cancelEdit').addEventListener('click', () => {
    document.getElementById('editPopover').style.display = 'none';
});

document.getElementById('deleteAll').addEventListener('click', () => {
    deleteAllTasks();
});

async function deleteAllTasks() {
    const userConfirmed = confirm("Are you sure you want to delete All task?");
    if (!userConfirmed) return;

    await axios.delete(`${API_URL}/delete-all`);
    fetchTasks();
}

async function deleteTask(id) {
    const userConfirmed = confirm("Are you sure you want to delete this task?");
    if (!userConfirmed) return;

    await axios.delete(`${API_URL}/${id}`);
    fetchTasks();
}

taskList.addEventListener('change', async (e) => {
    if (e.target.classList.contains('statusSelect')) {
        const id = e.target.dataset.id;
        const status = e.target.value;
        try {
            await axios.put(`${API_URL}/${id}/status`, { status });
            errorContainer.innerHTML = "";
            fetchTasks();
        } catch (error) {
            const apiErrors = error.response?.data?.errors;
            const errorMessages = apiErrors ? Object.values(apiErrors) : [];

            if (errorMessages.length > 0) {
                errorContainer.innerHTML = errorMessages.map(msg => `<p>${msg}</p>`).join("");
            }
            console.error("API Error on status change:", error.response ? error.response.data : error.message);
        }
    }
});


async function checkDatabaseConnectionAndFetchTasks() {
  try {
    const res = await axios.get(API_URL);

    updateConnectionStatus(true);
    taskList.innerHTML = '';
    res.data.forEach(task => addTaskToDOM(task));

  } catch (error) {
    updateConnectionStatus(false);
    console.error("Database connection error:", error.message);

    setTimeout(checkDatabaseConnectionAndFetchTasks, 20000);
    console.log("Retrying database connection check...");
  }
}

checkDatabaseConnectionAndFetchTasks();

function updateConnectionStatus(isConnected) {
  const dot = document.getElementById('statusDot');
  const text = document.getElementById('statusText');
  if (isConnected) {
    dot.style.backgroundColor = 'green';
    text.textContent = 'Connected';
    console.log("Connected to a database.");
  } else {
    dot.style.backgroundColor = 'red';
    text.textContent = 'Disconnected';
    console.log("Waiting for database connection...");
  }
}
