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
│   │   ├── servicosController.js
│   │   └── authController.js
│   ├── middlewares/
│   │   └── authMiddleware.js
│   ├── routes/
│   │   ├── usuarios.js
│   │   ├── fornecedores.js
│   │   ├── servicos.js
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
    "data_nascimento": "1999-05-15",
    "created_at": "2026-05-03T14:02:10.651Z"
  }
}
```

`400` — erro de validacao | `409` — duplicidade

---

#### GET `/usuarios` — Listar todos os usuarios

Sem body. Retorna array com todos os usuarios.

---

#### GET `/usuarios/:id` — Buscar usuario por ID

`404` se nao encontrado.

---

### 2. Fornecedores

#### POST `/fornecedores` — Cadastrar fornecedor

Salva dados pessoais em `usuarios` e dados da loja em `fornecedores` numa unica requisicao usando transacao.

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

> Quando `categoria` for `"Outros"`, adicionar `"categoria_outro": "Tatuagem"`.

**Categorias validas:**
`Beleza e Estetica` | `Saude` | `Alimentacao` | `Manutencao` | `Tecnologia` | `Outros`

**Respostas:**

`201` — fornecedor cadastrado:
```json
{
  "mensagem": "Fornecedor cadastrado com sucesso!",
  "usuario": { ... },
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

`400` — erro de validacao | `409` — duplicidade

---

#### GET `/fornecedores` — Listar todos os fornecedores

Sem body. Retorna dados pessoais e da loja unidos.

---

#### GET `/fornecedores/:id` — Buscar fornecedor por ID

`404` se nao encontrado.

---

### 3. Servicos

#### POST `/servicos` — Cadastrar servico
**Rota protegida — exige token JWT de fornecedor**

**Headers:**
```
Authorization: Bearer <token>
```

**Body:**
```json
{
  "nome": "Corte de cabelo",
  "descricao": "Corte feminino com lavagem e escova",
  "preco": 80,
  "categoria_id": 1
}
```

**Categorias (IDs):**
| ID | Nome |
|---|---|
| 1 | Beleza e Estetica |
| 2 | Saude |
| 3 | Alimentacao |
| 4 | Manutencao |
| 5 | Tecnologia |
| 6 | Outros |

**Respostas:**

`201` — servico cadastrado:
```json
{
  "mensagem": "Servico cadastrado com sucesso!",
  "servico": {
    "id": 1,
    "fornecedor_id": 1,
    "categoria_id": 1,
    "nome": "Corte de cabelo",
    "descricao": "Corte feminino com lavagem e escova",
    "preco": "80",
    "created_at": "2026-05-03T22:18:08.249Z"
  }
}
```

`400` — erro de validacao:
```json
{
  "erros": ["Nome do servico e obrigatorio."]
}
```

`403` — usuario nao e fornecedor:
```json
{
  "erro": "Apenas fornecedores podem cadastrar servicos."
}
```

`401` — token ausente ou invalido:
```json
{
  "erro": "Token nao fornecido."
}
```

---

#### GET `/servicos` — Listar todos os servicos
**Rota publica — sem token**

Retorna lista com dados do servico, categoria, loja e fornecedor.

**Resposta `200`:**
```json
[
  {
    "id": 1,
    "nome": "Corte de cabelo",
    "descricao": "Corte feminino com lavagem e escova",
    "preco": "80",
    "categoria": "Beleza e Estetica",
    "nome_loja": "Studio Marina",
    "nome_fornecedor": "Marina Silva",
    "cidade": "Fortaleza",
    "estado": "CE"
  }
]
```

---

#### GET `/servicos/:id` — Buscar servico por ID
**Rota publica — sem token**

Retorna detalhes completos do servico incluindo dados da loja.

---

#### GET `/servicos/fornecedor/:id` — Listar servicos de um fornecedor
**Rota publica — sem token**

Substituir `:id` pelo `usuario_id` do fornecedor.

Exemplo: `GET http://localhost:3000/servicos/fornecedor/5`

---

### 4. Autenticacao

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

`401` — credenciais invalidas.

---

## Observacoes para o front

1. **Mascaras:** aplicar mascara visualmente mas remover antes de enviar.
2. **Data de nascimento:** enviar no formato `YYYY-MM-DD`.
3. **Tipo:** usar o campo `tipo` retornado no login para redirecionar — cliente vai para tela de cliente, fornecedor para dashboard.
4. **Token expirado:** se a API retornar `401` em rota protegida, redirecionar para o login.
5. **Erros:** a API retorna todos os erros de uma vez no array `erros`.
6. **Idade minima:** cliente 16 anos, fornecedor 18 anos.
7. **Categoria Outros:** quando o usuario selecionar "Outros", exibir campo de texto e enviar `categoria_outro`.
8. **Servicos:** para cadastrar servico, enviar token no header. Para listar ou buscar, nao precisa de token.
9. **categoria_id dos servicos:** usar os IDs fixos da tabela de categorias (1 a 6).

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

// Cadastrar servico (protegido)
const responseServico = await fetch('http://localhost:3000/servicos', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`,
  },
  body: JSON.stringify({
    nome: 'Corte de cabelo',
    descricao: 'Corte feminino com lavagem e escova',
    preco: 80,
    categoria_id: 1,
  }),
});

// Listar servicos (publico)
const responseListar = await fetch('http://localhost:3000/servicos');
const servicos = await responseListar.json();
