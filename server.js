const mysql = require('mysql');
const express = require('express');
const app = express();

const PORT = process.env.PORT || 8005;
const MYSQL_DB_NAME = process.env.MYSQL_DB_NAME || "baza";
const MYSQL_TABLE_NAME = process.env.MYSQL_TABLE_NAME || "osoby";

const CREATE_DB_QUERY = `CREATE DATABASE IF NOT EXISTS ${MYSQL_DB_NAME};`;
const CREATE_TABLE_QUERY = `CREATE TABLE IF NOT EXISTS ${MYSQL_DB_NAME}.${MYSQL_TABLE_NAME}`
                         + `(imie VARCHAR(255) NOT NULL, nazwisko VARCHAR(255) NOT NULL, wiek INT NOT NULL ) ENGINE = InnoDB;`;
const INSERT_RECORDS_QUERY = `INSERT INTO ${MYSQL_DB_NAME}.${MYSQL_TABLE_NAME} (imie, nazwisko, wiek) VALUES`
                           + `('Mateusz', 'Kozak', '23'), ('Jan', 'Kowalski', '32'), ('Anna', 'Kowalska', '31');`;
const SELECT_RECORDS_QUERY = `SELECT * FROM ${MYSQL_DB_NAME}.${MYSQL_TABLE_NAME};`;

const connection = mysql.createConnection({
    host: process.env.MYSQL_HOST || 'localhost',
    port: process.env.MYSQL_PORT || '3306',
    user: process.env.MYSQL_USER || 'root',
    password: process.env.MYSQL_PASS || ''
});

const queryDb = (query, msg) => {
    connection.query(query, (err, result) => {
        if (err) {
            console.log(err);
            return;
        }

        console.log(msg);
    });
}

connection.connect((err) => {
    if (err) {
        console.log(err);
        return;
    }

    console.log("Połączono z bazą danych.");

    queryDb(CREATE_DB_QUERY, "Utworzono bazę danych.");

    queryDb(CREATE_TABLE_QUERY, "Utworzono tabelę.");

    queryDb(INSERT_RECORDS_QUERY, "Dodano rekordy do tabeli.");
});

app.get('/', (req, res) => {
    connection.query(SELECT_RECORDS_QUERY, (err, result) => {
        if (err) {
            console.log(err);
            res.send("Błąd pobierania danych z tabeli!");
            return;
        }

        console.log("Pobrano rekordy z tabeli.");

        let table = "<table border=1>";
        table += `<thead><tr> <th>imie</th>`;
        table += `<th>nazwisko</th>`;
        table += `<th>wiek</th> </tr><thead><tbody>`;

        result.forEach(wiersz => {
            table += `<tr> <td>${wiersz.imie}</td>`;
            table += `<td>${wiersz.nazwisko}</td>`;
            table += `<td>${wiersz.wiek}</td> </tr>`;
        });

        table += "</tbody></table>";

        res.send(table);
    });
});

app.listen(PORT, () => {
    console.log(`Aplikacja uruchomiona, nasłuchiwanie portu ${PORT}.`);
});