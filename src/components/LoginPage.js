// src/components/LoginPage.js

import React from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import logo from '../assets/logo.png';
import './LoginPage.css';


// Estilos para o layout e os campos de login
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
`;

const LoginBox = styled.div`
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
`;

const Logo = styled.img`
  width: 120px;
  margin-bottom: 1.5rem;
`;

const Title = styled.h2`
  color: #4caf50;
  margin-bottom: 1rem;
`;

const Input = styled.input`
  width: 100%;
  padding: 0.8rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  box-sizing: border-box;
`;

const Button = styled.button`
  width: 100%;
  padding: 0.8rem;
  background-color: #606062;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
  box-sizing: border-box;

  &:hover {
    background-color: #FDD141;
  }
`;

function LoginPage() {
  const navigate = useNavigate();

  const handleLogin = () => {
    // Redireciona para a página de atendimento após o login
    navigate('/attendance');
  };

  return (
    <Container>
      <LoginBox>
        <Logo src={logo} alt="Logo" />
        <h2 className="welcome-title">OLÁ, SEJA BEM-VINDO</h2>
        <Input type="text" placeholder="Usuário" />
        <Input type="password" placeholder="Senha" />
        <Button onClick={handleLogin}>Entrar</Button>
      </LoginBox>
    </Container>
  );
}

export default LoginPage;
