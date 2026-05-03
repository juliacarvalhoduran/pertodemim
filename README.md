# Perto de Mim — API Backend

API REST do aplicativo Perto de Mim, desenvolvida em Node.js com Express e PostgreSQL.

---

## Tecnologias

- Node.js com ES Modules
- Express
- PostgreSQL
- bcrypt (senhas)
- JWT (autenticacao)
- dotenv (variaveis de ambiente)

---

## Como rodar o projeto

### Pre-requisitos
- Node.js instalado
- PostgreSQL instalado e rodando

### Configuracao

1. Clone o repositorio:
```bash
git clone https://github.com/marinacpontes/PertoDeMim.git
```

2. Instale as dependencias:
```bash
npm install
```

3. Crie o arquivo `.env` na raiz do projeto com as variaveis:
```
DB_USER=postgres
DB_HOST=localhost
DB_NAME=perto_de_mim
DB_PASSWORD=sua_senha
DB_PORT=5432
JWT_SECRET=uma_chave_secreta_longa
```

4. Rode o servidor:
```bash
node src/app.js
```

O servidor sobe em `http://localhost:3000`

---

## Estrutura do projeto

```
backend-app/
├── src/
│   ├── config/
│   │   └── db.js               # Conexao com o banco
│   ├── controllers/
│   │   ├── usuariosController.js
│   │   └── authController.js
│   ├── middlewares/
│   │   └── authMiddleware.js    # Protege rotas com JWT
│   ├── routes/
│   │   ├── usuarios.js
│   │   └── auth.js
│   └── app.js
├── .env                         # NAO sobe no Git, aqui fica suas senhas. 
├── .env.example                 # Modelo sem valores reais
├── .gitignore
└── package.json
```

---

## Autenticacao

Rotas protegidas exigem token JWT no header:

```
Authorization: Bearer <token>
```

O token e obtido no login e expira em 7 dias.

---

## Rotas

### Usuarios

#### POST `/usuarios` — Criar usuario

**Body:**
```json
{
  "nome": "Julia",
  "email": "julia@email.com",
  "senha": "123456",
  "tipo": "cliente",
  "telefone": "85999999999",
  "cpf_cnpj": "52998224725",
  "data_nascimento": "1999-05-15",
  "logradouro": "Rua das Flores",
  "cep": "60000000",
  "numero": "123",
  "bairro": "Meireles",
  "complemento": "Apto 42",
  "cidade": "Fortaleza",
  "estado": "CE"
}
```

**Regras de validacao:**
| Campo | Regra |
|---|---|
| nome | Obrigatorio, max 40 caracteres |
| email | Obrigatorio, formato valido. Nao pode ser duplicado |
| senha | Obrigatorio, minimo 6 caracteres |
| tipo | Obrigatorio: `"cliente"` ou `"fornecedor"` |
| telefone | Obrigatorio, DDD valido + numero (so digitos, 10 ou 11) |
| cpf_cnpj | Obrigatorio, CPF (11 digitos) ou CNPJ (14 digitos). Nao pode ser duplicado |
| data_nascimento | Obrigatorio, formato `YYYY-MM-DD`. Cliente: min 16 anos. Fornecedor: min 18 anos |
| logradouro | Obrigatorio, max 50 caracteres |
| cep | Obrigatorio, 8 digitos |
| numero | Obrigatorio |
| bairro | Obrigatorio |
| cidade | Obrigatorio |
| estado | Obrigatorio, sigla ex: `"CE"` |
| complemento | Opcional |

> Enviar telefone, cpf_cnpj e cep **sem mascara** — so os digitos.

**Respostas:**

`201` — usuario criado:
```json
{
  "mensagem": "Usuario criado com sucesso!",
  "usuario": {
    "id": 1,
    "nome": "Julia",
    "email": "julia@email.com",
    "telefone": "85999999999",
    "cpf_cnpj": "52998224725",
    "tipo": "cliente",
    "logradouro": "Rua das Flores",
    "cep": "60000000",
    "numero": "123",
    "bairro": "Meireles",
    "complemento": "Apto 42",
    "cidade": "Fortaleza",
    "estado": "CE",
    "data_nascimento": "1999-05-15",
    "created_at": "2026-05-03T14:02:10.651Z"
  }
}
```

`400` — erro de validacao:
```json
{
  "erros": ["Fornecedor deve ter no minimo 18 anos."]
}
```

`409` — email ou cpf_cnpj duplicado:
```json
{
  "erro": "E-mail ja cadastrado."
}
```

---

#### GET `/usuarios` — Listar todos os usuarios

Sem body. Retorna array com todos os usuarios.

---

#### GET `/usuarios/:id` — Buscar usuario por ID

Sem body. Substituir `:id` pelo numero do usuario.

`404` se nao encontrado:
```json
{
  "erro": "Usuario nao encontrado."
}
```

---

### Autenticacao

#### POST `/auth/login` — Login

**Body:**
```json
{
  "email": "julia@email.com",
  "senha": "123456"
}
```

**Resposta `200`:**
```json
{
  "mensagem": "Login realizado com sucesso!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nome": "Julia",
    "email": "julia@email.com",
    "tipo": "cliente"
  }
}
```

`401` — credenciais invalidas:
```json
{
  "erro": "E-mail ou senha invalidos."
}
```

---

## Observacoes para o front

1. **Mascaras:** aplicar mascara visualmente mas remover antes de enviar.
2. **Data de nascimento:** enviar no formato `YYYY-MM-DD`.
3. **Tipo:** usar o campo `tipo` retornado no login para redirecionar — cliente vai para tela de cliente, fornecedor para dashboard.
4. **Token expirado:** se a API retornar `401` em rota protegida, redirecionar para o login.
5. **Erros:** a API retorna todos os erros de uma vez no array `erros`.
6. **Idade minima:** cliente 16 anos, fornecedor 18 anos — validar no front tambem para melhor experiencia.

---

## Exemplo em JavaScript

```javascript
// Cadastro
const response = await fetch('http://localhost:3000/usuarios', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ ...dadosDoFormulario }),
});
const data = await response.json();

// Login
const responseLogin = await fetch('http://localhost:3000/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, senha }),
});
const { token, usuario } = await responseLogin.json();

// Requisicao protegida
const responseProt = await fetch('http://localhost:3000/rota-protegida', {
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`,
  },
});
```