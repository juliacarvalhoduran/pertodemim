import express from 'express';
import cors from 'cors';
import usuariosRoutes from './routes/usuarios.js';
import authRoutes from './routes/auth.js';
import fornecedoresRoutes from './routes/fornecedores.js';
import servicosRoutes from './routes/servicos.js';
import pedidosRoutes from './routes/pedidos.js';



const app = express();

app.use(cors());
app.use(express.json());
app.use('/usuarios', usuariosRoutes);
app.use('/auth', authRoutes);
app.use('/fornecedores', fornecedoresRoutes);
app.use('/servicos', servicosRoutes);
app.use('/pedidos', pedidosRoutes);

app.get('/', (req, res) => {
  res.send('API rodando');
});

app.listen(3000, '0.0.0.0', () => {
  console.log('Servidor rodando em http://0.0.0.0:3000');
});