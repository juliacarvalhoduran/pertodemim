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
│   │   └── db.js
│   ├── controllers/
│   │   ├── usuariosController.js
│   │   ├── fornecedoresController.js
│   │   └── authController.js
│   ├── middlewares/
│   │   └── authMiddleware.js
│   ├── routes/
│   │   ├── usuarios.js
│   │   ├── fornecedores.js
│   │   └── auth.js
│   └── app.js
├── .env                  # NAO sobe no Git, aqui ficam suas senhas
├── .env.example          # Modelo sem valores reais
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

### 1. Usuarios

#### POST `/usuarios` — Criar cliente

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
> Data de nascimento no formato `YYYY-MM-DD`.

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
  "erros": ["Cliente deve ter no minimo 16 anos."]
}
```

`409` — duplicidade:
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

`404` se nao encontrado.

---

### 2. Fornecedores

#### POST `/fornecedores` — Cadastrar fornecedor

Salva os dados pessoais em `usuarios` e os dados da loja em `fornecedores` numa unica requisicao, usando transacao — se qualquer parte falhar, nada e salvo.

**Body:**
```json
{
  "nome": "Marina Silva",
  "email": "marina@email.com",
  "senha": "123456",
  "telefone": "85988887777",
  "cpf_cnpj": "11144477735",
  "data_nascimento": "1995-03-20",
  "logradouro": "Av. Beira Mar",
  "cep": "60165121",
  "numero": "100",
  "bairro": "Meireles",
  "cidade": "Fortaleza",
  "estado": "CE",
  "nome_loja": "Studio Marina",
  "nome_responsavel": "Marina Silva",
  "categoria": "Beleza e Estetica",
  "descricao": "Especialista em cabelo e maquiagem",
  "preco_medio": 80
}
```

> Quando `categoria` for `"Outros"`, adicionar o campo `categoria_outro`:
```json
{
  "categoria": "Outros",
  "categoria_outro": "Tatuagem"
}
```

**Categorias validas:**
- `Beleza e Estetica`
- `Saude`
- `Alimentacao`
- `Manutencao`
- `Tecnologia`
- `Outros` (requer `categoria_outro`)

**Regras de validacao — dados pessoais:**
| Campo | Regra |
|---|---|
| nome | Obrigatorio, max 40 caracteres |
| email | Obrigatorio, formato valido. Nao pode ser duplicado |
| senha | Obrigatorio, minimo 6 caracteres |
| telefone | Obrigatorio, DDD valido + numero (so digitos) |
| cpf_cnpj | Obrigatorio, CPF ou CNPJ valido. Nao pode ser duplicado |
| data_nascimento | Obrigatorio, formato `YYYY-MM-DD`. Minimo 18 anos |
| logradouro | Obrigatorio, max 50 caracteres |
| cep | Obrigatorio, 8 digitos |
| numero | Obrigatorio |
| bairro | Obrigatorio |
| cidade | Obrigatorio |
| estado | Obrigatorio, sigla ex: `"CE"` |
| complemento | Opcional |

**Regras de validacao — dados da loja:**
| Campo | Regra |
|---|---|
| nome_loja | Obrigatorio |
| nome_responsavel | Obrigatorio |
| categoria | Obrigatorio, deve ser uma das categorias validas |
| categoria_outro | Obrigatorio apenas quando categoria for `"Outros"` |
| descricao | Obrigatorio |
| preco_medio | Obrigatorio, numero positivo |

**Respostas:**

`201` — fornecedor cadastrado:
```json
{
  "mensagem": "Fornecedor cadastrado com sucesso!",
  "usuario": {
    "id": 10,
    "nome": "Marina Silva",
    "email": "marina@email.com",
    "telefone": "85988887777",
    "cpf_cnpj": "11144477735",
    "tipo": "fornecedor",
    "logradouro": "Av. Beira Mar",
    "cep": "60165121",
    "numero": "100",
    "bairro": "Meireles",
    "complemento": null,
    "cidade": "Fortaleza",
    "estado": "CE",
    "data_nascimento": "1995-03-20",
    "created_at": "2026-05-03T21:44:14.659Z"
  },
  "loja": {
    "id": 4,
    "usuario_id": 10,
    "nome_loja": "Studio Marina",
    "nome_responsavel": "Marina Silva",
    "categoria": "Beleza e Estetica",
    "categoria_outro": null,
    "descricao": "Especialista em cabelo e maquiagem",
    "preco_medio": "80"
  }
}
```

`400` — erro de validacao:
```json
{
  "erros": [
    "Fornecedor deve ter no minimo 18 anos.",
    "Ao selecionar \"Outros\", descreva a categoria."
  ]
}
```

`409` — duplicidade:
```json
{
  "erro": "E-mail ja cadastrado."
}
```

---

#### GET `/fornecedores` — Listar todos os fornecedores

Sem body. Retorna array com dados pessoais e da loja unidos.

---

#### GET `/fornecedores/:id` — Buscar fornecedor por ID

Sem body. Substituir `:id` pelo ID do usuario.

`404` se nao encontrado.

---

### 3. Autenticacao

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
6. **Idade minima:** cliente 16 anos, fornecedor 18 anos.
7. **Categoria Outros:** quando o usuario selecionar "Outros", exibir campo de texto e enviar `categoria_outro` no body.

---

## Exemplo em JavaScript

```javascript
// Cadastro de cliente
const response = await fetch('http://localhost:3000/usuarios', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ ...dadosDoFormulario }),
});

// Cadastro de fornecedor
const responseForn = await fetch('http://localhost:3000/fornecedores', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ ...dadosPessoais, ...dadosDaLoja }),
});

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
