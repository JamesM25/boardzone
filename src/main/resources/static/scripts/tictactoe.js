const API_URL = `${window.origin}/api/tictactoe`;

const BOARD_AREA = 9;

const tictactoe = document.getElementById("tictactoe");
const boardElements = [];

let game = {
    board: [
        0, 0, 0,
        0, 0, 0,
        0, 0, 0
    ],
    winner: 0,
    difficulty: 0
};
let history = {
    id: 0,
    moves: [],
    difficulty: 1
};

let waiting = false;

const SYMBOL = [
    "", "X", "O"
];
const SYMBOL_ID = {
    "": 0,
    "NONE": 0,
    "X": 1,
    "O": 2
};

function placeSymbol(index) {
    if (game.board[index] !== 0 || waiting || game.winner !== 0) {
        return;
    }

    game.board[index] = 1;
    history.moves.push(index);
    updateBoard();

    waiting = true;

    fetch(`${API_URL}/game`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(game)
    }).then(response => {
        if (response.status !== 200) {
            return;
        }

        response.json().then(async data => {
            let cpuMove = -1;

            for (let i = 0; i < BOARD_AREA; i++) {
                const oldSymbol = game.board[i];
                const newSymbol = SYMBOL_ID[data.board[i]];

                if (newSymbol !== oldSymbol) {
                    game.board[i] = newSymbol;

                    if (cpuMove >= 0) {
                        throw new Error("Opponent cannot claim multiple tiles in a single turn");
                    }

                    cpuMove = i;
                }
            }

            game.winner = SYMBOL_ID[data.winner];

            if (cpuMove < 0 && game.winner !== 0) {
                throw new Error("Opponent cannot skip their turn");
            }

            if (cpuMove >= 0) {
                history.moves.push(cpuMove);
            }

            history = await fetch(`${API_URL}/history`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(history)
            }).then(response => response.json());

            updateBoard();
            waiting = false;
        });
    });
}

function updateBoard() {
    for (let i = 0; i < BOARD_AREA; i++) {
        boardElements[i].textContent = SYMBOL[game.board[i]];
    }

    const hasWinner = game.winner !== 0;
    const activeClass = "active";
    if (hasWinner && tictactoe.classList.contains(activeClass)) {
        tictactoe.classList.remove(activeClass);
    } else if (!hasWinner && !tictactoe.classList.contains(activeClass)) {
        tictactoe.classList.add(activeClass);
    }
}

function initBoard() {
    for (let i = 0; i < BOARD_AREA; i++) {
        const space = tictactoe.appendChild(document.createElement("div"));
        space.onclick = () => { placeSymbol(i); };
        boardElements.push(space);
    }
}
async function initGame() {
    const response = await fetch(`${API_URL}/history`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(history)
    });

    if (!response.ok) {
        throw new Error("Response was not OK");
    }

    /* Update the history object to store the ID assigned by the server.
       Otherwise, PUT requests won't update the correct resource. */
    history = await response.json()

    tictactoe.classList.add("active");
    tictactoe.classList.remove("invisible");

    updateBoard();
}

async function viewHistory(game) {
    const result = await fetch(`${API_URL}/history/${game}`, {
        method: "GET"
    });
    const data = await result.json();

    // Prevent the user from clicking the board
    waiting = true;

    document.getElementById("history-list").classList.add("invisible");

    tictactoe.classList.remove("invisible");
    tictactoe.classList.remove("active");


    let index = 0;

    setInterval(() => {
        if (index >= data.moves.length) {
            for (const elem of boardElements) {
                elem.textContent = "";
            }
            index = 0;
            return;
        }

        const symbol = index % 2 === 0 ? "X" : "O";
        boardElements[data.moves[index]].textContent = symbol;

        index++;
    }, 1000);
}

async function renameHistory(game) {
    const result = await fetch(`${API_URL}/history/${game}`, {
        method: "GET"
    });
    const data = await result.json();

    data.name = prompt("Please enter a new name", data.name);
    if (data.name !== null) {
        await fetch(`${API_URL}/history`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        refreshHistory();
    }
}

async function deleteHistory(game) {
    const result = await fetch(`${API_URL}/history/${game}`, {
        method: "DELETE"
    });

    refreshHistory();
}

async function refreshHistory() {
    const result = await fetch(`${API_URL}/history`, {
        method: "GET"
    });
    const data = await result.json();

    const container = document.querySelector("#history-list .history-container");
    container.innerHTML = "";

    // data.sort((a, b) => a.date.localeCompare(b.date));

    for (const game of data) {
        container.innerHTML += `
            <div class="history">
                <h3>${game.name}</h3>
                <p>Completed in ${game.moves.length} moves</p>
                <p>${game.date}</p>
                <button onclick="viewHistory(${game.id})">View</button>
                <button onclick="renameHistory(${game.id})">Rename</button>
                <button onclick="deleteHistory(${game.id})">Delete</button>
            </div>
        `;
    }
}

initBoard();

document.getElementById("btn-play").onclick = () => {
    document.getElementById("initial-select").classList.add("invisible");
    document.getElementById("difficulty-select").classList.remove("invisible");
};
document.getElementById("btn-history").onclick = () => {
    document.getElementById("initial-select").classList.add("invisible");
    document.getElementById("history-list").classList.remove("invisible");
    refreshHistory();
};

for (const btn of document.querySelectorAll("#difficulty-select button")) {
    btn.onclick = () => {
        game.difficulty = history.difficulty = btn.dataset.difficulty;
        document.getElementById("difficulty-select").classList.add("invisible");
        initGame();
    }
}