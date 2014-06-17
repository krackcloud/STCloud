-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-06-2014 a las 09:10:03
-- Versión del servidor: 5.6.16
-- Versión de PHP: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `stcloud`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivo`
--

CREATE TABLE IF NOT EXISTS `archivo` (
  `idArchivo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) DEFAULT NULL,
  `fechaCreacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ruta` varchar(100) DEFAULT NULL,
  `idCarpeta` int(11) DEFAULT NULL,
  PRIMARY KEY (`idArchivo`),
  KEY `idCarpeta` (`idCarpeta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carpeta`
--

CREATE TABLE IF NOT EXISTS `carpeta` (
  `idCarpeta` int(11) NOT NULL AUTO_INCREMENT,
  `cpt_nombre` varchar(40) DEFAULT NULL,
  `cpt_fechaCreacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cpt_ruta` varchar(100) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  `idGrupo` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCarpeta`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idGrupo` (`idGrupo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE IF NOT EXISTS `grupo` (
  `idGrupo` int(11) NOT NULL AUTO_INCREMENT,
  `grp_nombre` varchar(30) NOT NULL,
  `grp_descripcion` varchar(100) DEFAULT NULL,
  `grp_fechaCreacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `idUsuarioAdmin` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGrupo`),
  KEY `idUsuarioAdmin` (`idUsuarioAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `usr_nombre` varchar(50) DEFAULT NULL,
  `usr_genero` enum('M','F') DEFAULT 'F',
  `usr_fechaNacimiento` date DEFAULT NULL,
  `usr_email` varchar(50) DEFAULT NULL,
  `usr_usuario` char(30) DEFAULT NULL,
  `usr_contrasenia` varchar(800) DEFAULT NULL,
  `usr_privilegios` enum('USUARIO','ADMINISTRADOR') DEFAULT 'USUARIO',
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_grupo`
--

CREATE TABLE IF NOT EXISTS `usuario_grupo` (
  `idLogUnion` int(11) NOT NULL AUTO_INCREMENT,
  `log` varchar(30) DEFAULT NULL,
  `usrgrp_idUsuario` int(11) DEFAULT NULL,
  `usrgrp_idGrupo` int(11) DEFAULT NULL,
  PRIMARY KEY (`idLogUnion`),
  KEY `usrgrp_idUsuario` (`usrgrp_idUsuario`),
  KEY `usrgrp_idGrupo` (`usrgrp_idGrupo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `archivo`
--
ALTER TABLE `archivo`
  ADD CONSTRAINT `archivo_ibfk_1` FOREIGN KEY (`idCarpeta`) REFERENCES `carpeta` (`idCarpeta`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `carpeta`
--
ALTER TABLE `carpeta`
  ADD CONSTRAINT `carpeta_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `carpeta_ibfk_2` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`idGrupo`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD CONSTRAINT `grupo_ibfk_1` FOREIGN KEY (`idUsuarioAdmin`) REFERENCES `usuario` (`idUsuario`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario_grupo`
--
ALTER TABLE `usuario_grupo`
  ADD CONSTRAINT `usuario_grupo_ibfk_1` FOREIGN KEY (`usrgrp_idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `usuario_grupo_ibfk_2` FOREIGN KEY (`usrgrp_idGrupo`) REFERENCES `grupo` (`idGrupo`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
