import express from 'express';
import cors from 'cors';
import usuariosRoutes from './routes/usuarios.js';
import authRoutes from './routes/auth.js';
import fornecedoresRoutes from './routes/fornecedores.js';
import servicosRoutes from './routes/servicos.js';


const app = express();

app.use(cors());
app.use(express.json());
app.use('/usuarios', usuariosRoutes);
app.use('/auth', authRoutes);
app.use('/fornecedores', fornecedoresRoutes);
app.use('/servicos', servicosRoutes);

app.get('/', (req, res) => {
  res.send('API rodando');
});

app.listen(3000, () => {
  console.log('Servidor rodando na porta 3000');
});