const API_URL = "http://localhost:8080/api/tictactoe";

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

    if (!response.ok) throw new Error();

    /* Update the history object to store the ID assigned by the server.
       Otherwise, PUT requests won't update the correct resource. */
    history = await response.json()

    tictactoe.classList.add("active");
    tictactoe.classList.remove("invisible");

    updateBoard();
}

initBoard();

for (const btn of document.querySelectorAll("#difficulty-select button")) {
    btn.onclick = () => {
        game.difficulty = history.difficulty = btn.dataset.difficulty;
        document.getElementById("difficulty-select").classList.add("invisible");
        initGame();
    }
}