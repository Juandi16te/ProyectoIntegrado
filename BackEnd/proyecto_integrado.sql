
/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`proyecto_integrado` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `proyecto_integrado`;

/*Table structure for table `ofertante` */
DROP TABLE IF EXISTS `ofertante`;
CREATE TABLE `ofertante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `foto` LONGTEXT,
  `valoracion` decimal(3,2),
  `descripcion` varchar(1000),
  `fecha_creacion_usuario` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `consumidor` */
DROP TABLE IF EXISTS `consumidor`;
CREATE TABLE `consumidor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `foto` LONGTEXT,
  `fecha_creacion_usuario` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tipo_actividad` */
DROP TABLE IF EXISTS `tipo_actividad`;
CREATE TABLE `tipo_actividad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `actividad` */
DROP TABLE IF EXISTS `actividad`;
CREATE TABLE `actividad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(1500) NOT NULL,
  `fecha_inicio` DATETIME NOT NULL,
  `fecha_fin` DATETIME NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `ubicacion` LONGTEXT NOT NULL,
  `num_participantes` int(3) NOT NULL,
  `num_participantes_total` int(3) NOT NULL,
  `valoracion` decimal(3,2),
  `id_ofertante` int(11),
  `id_tipo_actividad` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_ofertante` (`id_ofertante`),
  KEY `id_tipo_actividad` (`id_tipo_actividad`),
  CONSTRAINT `actividad_fk_1` FOREIGN KEY (`id_ofertante`) REFERENCES `ofertante` (`id`),
  CONSTRAINT `actividad_fk_2` FOREIGN KEY (`id_tipo_actividad`) REFERENCES `tipo_actividad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `actividad_consumidor` */
DROP TABLE IF EXISTS `actividad_consumidor`;
CREATE TABLE `actividad_consumidor` (
  `id_actividad` int(11) NOT NULL,
  `id_consumidor` int(11),
  `valoracion` decimal(3,2),
  PRIMARY KEY (`id_actividad`,`id_consumidor`),
  KEY `id_actividad` (`id_actividad`),
  KEY `id_consumidor` (`id_consumidor`),
  CONSTRAINT `actividad_consumidor_fk_1` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id`),
  CONSTRAINT `actividad_consumidor_fk_2` FOREIGN KEY (`id_consumidor`) REFERENCES `consumidor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;