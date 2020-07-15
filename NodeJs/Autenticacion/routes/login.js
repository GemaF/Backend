const express = require('express');
// const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Usuario = require('../models/usuario');
const verificaToken = require('../middlewares/autenticacion');


const app = express();


// Para realizar la autenticacion:
app.post('/login', (req, res) => {

    let body = req.body;

    // Comprobamos si el usuario/email existe:
    Usuario.findOne({ user: body.user }, (err, usuarioDB) => {

        if (err) {
            return res.status(500).json({
                ok: false,
                err
            });
        }

        // Si el usuario no esta en la bbdd:
        if (!usuarioDB) {
            return res.status(400).json({
                ok: false,
                err: {
                    message: '(Usuario) o contraseña incorrectos'
                }
            });
        }

        // Falla la contrasenia:
        // if (!bcrypt.compareSync(body.password === usuarioDB.password)) {
        if (!(body.password === usuarioDB.password)) {
            return res.status(400).json({
                ok: false,
                err: {
                    message: 'Usuario o (contraseña) incorrectos'
                }
            });
        }


        // Generamos el token:
        let token = jwt.sign({
            usuario: usuarioDB
        }, process.env.SEED, { expiresIn: process.env.CADUCIDAD_TOKEN });

        res.json({
            ok: true,
            usuario: usuarioDB,
            token
        });


    });
});

app.get('/login', verificaToken, (req, res) => {

    Usuario.find({ estado: true }, 'user')
        .exec((err, usuarios) => {

            if (err) {
                return res.status(400).json({
                    ok: false,
                    err
                });
            }
        });


    res.json({
        ok: true,
        message: 'Token correcto'
    });

});

module.exports = app;