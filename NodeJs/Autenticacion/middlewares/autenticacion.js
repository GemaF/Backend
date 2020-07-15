const jwt = require('jsonwebtoken');

// =====================
// Verificar Token
// =====================
let verificaToken = (req, res, next) => {

    // Recibo el token (leo el token, obtengo el headers):
    let token = req.get('token');

    // Comprobamos q el token es valido
    jwt.verify(token, process.env.SEED, (err, decoded) => {

        if (err) {
            return res.status(401).json({
                ok: false,
                err: {
                    message: 'Token no valido'
                }
            });
        }

        // Token ok: accdedemos a la informacion del usuario.
        req.usuario = decoded.usuario;
        next();

    });

};

module.exports = verificaToken;